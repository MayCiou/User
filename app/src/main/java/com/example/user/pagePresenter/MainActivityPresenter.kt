package com.example.user.pagePresenter

import android.content.Context
import android.util.Log
import com.example.user.pageInterface.MainActivityView
import com.example.user.server.RetrofitHttp
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivityPresenter(private val context : Context) {

    private lateinit var mainActivityView : MainActivityView
    private val tag = javaClass.simpleName

    fun setView(mainActivityView: MainActivityView) {

        this.mainActivityView = mainActivityView
    }

    fun getUserList(service: RetrofitHttp){

        if(!service.initClient())return

        service.getApi().getUsers()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                Log.i(tag, "getUserList : ${it.code()}")
            },
            {

                Log.e(tag, "getUserList : $it")

            }, {

            }, {

            })
    }
}