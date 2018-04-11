package com.dinophan.authapp.bases

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.os.Bundle
import android.os.PersistableBundle
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast


abstract class BaseActivity<T>(@LayoutRes private val layoutResId: Int? = null): AppCompatActivity() where T: ViewDataBinding {

    abstract fun onActivityCreated(dataBinder: T)
    fun getBinder(): T? = this@BaseActivity._binder
    private var _binder: T? = null

    final override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        this@BaseActivity.initial()
    }

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this@BaseActivity.initial()
    }

    private fun initial() {
        this@BaseActivity.layoutResId?.let { layoutId ->
            val viewBinder = DataBindingUtil.setContentView<T>(
                    this@BaseActivity,
                    layoutId
            )
            this@BaseActivity._binder = viewBinder
            this@BaseActivity.onActivityCreated(viewBinder)
        }
    }

    protected fun print(message: String, vararg args: Any?) {
        Log.d(this@BaseActivity.localClassName, String.format(message, args))
    }

    protected fun toast(message: String, vararg args: Any?) {
        Toast.makeText(this@BaseActivity, String.format(message, args), Toast.LENGTH_SHORT).show()
    }

}