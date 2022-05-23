package com.example.user.pagePresenter

import android.content.Context
import android.util.Log
import com.example.user.R
import com.example.user.pageInterface.MainActivityView
import com.example.user.server.RetrofitHttp
import com.example.user.struct.UserItem
import com.example.user.utils.NetworkUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainActivityPresenter(private val context : Context) {

    private lateinit var mainActivityView : MainActivityView
    private val tag = javaClass.simpleName

    fun setView(mainActivityView: MainActivityView) {

        this.mainActivityView = mainActivityView
    }

    fun getUserList(service: RetrofitHttp){

        if(!NetworkUtil.isNetworkConnected(context))
        {
            mainActivityView.showAlert(
                context.getString(R.string.message),
                context.getString(R.string.no_network_msg)){}
            return
        }

        if(!service.initClient())return

        mainActivityView.showLoadingProgressDialog()
        service.getApi().getUsers(0, 100)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                Log.i(tag, "getUserList : ${it.code()}, count :${it.body()?.size?:-1}")
                mainActivityView.showLoadingProgressDialog()
                mainActivityView.updateListView(it.body()?:ArrayList<UserItem>())
            },
            {

                Log.e(tag, "getUserList : $it")
                mainActivityView.dismissLoadingProgressDialog()
            }, {
                mainActivityView.dismissLoadingProgressDialog()
            }, {

            })
    }
}