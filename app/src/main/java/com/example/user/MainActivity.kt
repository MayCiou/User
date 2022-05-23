package com.example.user

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.user.pageInterface.MainActivityView
import com.example.user.pagePresenter.MainActivityPresenter
import com.example.user.server.RetrofitHttp

class MainActivity : AppCompatActivity() , MainActivityView {

    private var mainActivityPresenter : MainActivityPresenter? = null
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
        mainActivityPresenter!!.getUserList(service!!)
    }

    override fun onDestroy() {
        super.onDestroy()

        service?.destroy()
        mainActivityPresenter = null
        service = null
    }
}