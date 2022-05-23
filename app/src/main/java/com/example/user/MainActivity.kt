package com.example.user


import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.user.pageInterface.MainActivityView
import com.example.user.pagePresenter.MainActivityPresenter
import com.example.user.server.RetrofitHttp
import com.example.user.utils.LoadingProgressDialog

class MainActivity : AppCompatActivity() , MainActivityView {

    private var mainActivityPresenter : MainActivityPresenter? = null
    private var loadingProgressDialog : LoadingProgressDialog? = null
    private var service : RetrofitHttp? = null
    private lateinit var context : Context
    private val tag = javaClass.simpleName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        context = this
        service = RetrofitHttp()

        mainActivityPresenter = MainActivityPresenter(context)
        mainActivityPresenter!!.setView(this)

        loadingProgressDialog = LoadingProgressDialog(context)
        loadingProgressDialog!!.create()

    }

    override fun onResume() {
        super.onResume()

        mainActivityPresenter!!.getUserList(service!!)
    }

    override fun onDestroy() {
        super.onDestroy()

        service?.destroy()
        loadingProgressDialog?.dismiss()
        mainActivityPresenter = null
        loadingProgressDialog = null
        service = null
    }
}