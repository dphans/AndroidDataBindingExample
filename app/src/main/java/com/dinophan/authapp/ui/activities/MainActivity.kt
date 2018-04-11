package com.dinophan.authapp.ui.activities

import android.view.View
import com.dinophan.authapp.R
import com.dinophan.authapp.bases.BaseActivity
import com.dinophan.authapp.databinding.ActivityMainBinding
import com.dinophan.authapp.models.UserModel
import com.dinophan.authapp.modules.mock.APIMock
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivity : BaseActivity<ActivityMainBinding>(R.layout.activity_main) {

    private val userData: UserModel = UserModel()

    override fun onActivityCreated(dataBinder: ActivityMainBinding) {
        dataBinder.presenter            = Presenter()
        dataBinder.isRegisterationState = false
        dataBinder.isFormSubmitting     = false
        dataBinder.isFormValidated      = this@MainActivity.userData.validates()
    }

    @Suppress("UNUSED_PARAMETER")
    inner class Presenter {

        private var confirmationPassword: String = String()

        fun onUsernameChanged(text: CharSequence, start: Int, before: Int, count: Int) {
            this@MainActivity.userData.username = text.toString()
            this@Presenter.validates()
        }

        fun onPasswordChanged(text: CharSequence, start: Int, before: Int, count: Int) {
            this@MainActivity.userData.password = text.toString()
            this@Presenter.validates()
        }

        fun onConfirmationPasswordChanged(text: CharSequence, start: Int, before: Int, count: Int) {
            this@Presenter.confirmationPassword = text.toString()
            this@Presenter.validates()
        }

        fun onToggleRegisterationState(view: View) {
            this@MainActivity.getBinder()?.isRegisterationState = !(this@MainActivity.getBinder()?.isRegisterationState ?: false)
            this@Presenter.validates()
        }

        fun onFormSubmit(view: View) {
            this@MainActivity.getBinder()?.isFormSubmitting = true
            Observable.create<APIMock.APIResponseMock<UserModel>> {
                APIMock.UserAPI.signIn(this@MainActivity.userData, { signInResult ->
                    try {
                        if (signInResult.exception != null)
                            throw Exception(signInResult.exception)
                        if (signInResult.data == null)
                            throw Exception("Can't get user data from server!")
                        it.onNext(signInResult)
                    } catch (exception: Exception) {
                        it.onError(exception)
                    }
                })
            }
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.newThread())
                    .subscribe({
                        this@MainActivity.toast("Logged in as ${it.data!!.username}!")
                        this@MainActivity.getBinder()?.isFormSubmitting = false
                    }, {
                        this@MainActivity.getBinder()?.formErrorMessage = it.localizedMessage
                        this@MainActivity.getBinder()?.isFormSubmitting = false
                    })
        }

        private fun validates() {
            this@MainActivity.getBinder()?.isFormValidated  = this@MainActivity.userData.validates(if (this@MainActivity.getBinder()?.isRegisterationState == true) this@Presenter.confirmationPassword else null)
            this@MainActivity.getBinder()?.formErrorMessage = this@MainActivity.userData.errorMessage
        }

    }

}
