package com.dinophan.authapp.modules.mock

import android.os.Handler
import android.os.Looper
import com.dinophan.authapp.bases.BaseModel
import com.dinophan.authapp.models.UserModel
import com.google.gson.annotations.SerializedName
import java.util.*

object APIMock {

    data class APIResponseMock<T>(
            @SerializedName("data") var data: T? = null,
            @SerializedName("exception") var exception: String? = null
    ): BaseModel() where T: BaseModel

    object UserAPI {

        private val handler: Handler   = Handler(Looper.getMainLooper())
        private val callbacks: ArrayList<Runnable> = ArrayList()

        fun signIn(user: UserModel, response: (APIResponseMock<UserModel>) -> Unit) {
            this@UserAPI.callbacks.forEach { this@UserAPI.handler.removeCallbacks(it) }
            this@UserAPI.callbacks.clear()
            val signInCallback = Runnable {
                val isException = Random().nextBoolean()
                response.invoke(APIResponseMock(if (!isException) user else null, if (isException) this@UserAPI.getRandomError() else null))
            }
            this@UserAPI.callbacks.add(signInCallback)
            this@UserAPI.handler.postDelayed(signInCallback, 1000)
        }

        private fun getRandomError(): String {
            val errorMessages = arrayOf(
                    "No internet connection!",
                    "Internal server error!",
                    "Username or password is incorrect!",
                    "Unexpected error while processing your request!"
            )
            return errorMessages[Random().nextInt(errorMessages.count())]
        }

    }

}
