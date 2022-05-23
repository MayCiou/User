package com.example.user.pagePresenter

import android.content.Context
import android.util.Log
import com.example.user.R
import com.example.user.pageInterface.UserDetailView
import com.example.user.server.RetrofitHttp
import com.example.user.struct.UserItem
import com.example.user.utils.NetworkUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserDetailPresenter(private val context : Context) {

    private lateinit var userDetailView : UserDetailView
    private val tag = javaClass.simpleName

    fun setView(userDetailView: UserDetailView) {

        this.userDetailView = userDetailView
    }

    fun getUserDetail(service: RetrofitHttp){

        if(!NetworkUtil.isNetworkConnected(context))
        {
            userDetailView.showAlert(
                context.getString(R.string.message),
                context.getString(R.string.no_network_msg)){}
            return
        }

        if(!service.initClient())return

        userDetailView.showLoadingProgressDialog()
        service.getApi().getUsers(0, 100)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                Log.i(tag, "getUserDetail : ${it.code()}, count :${it.body()?.size?:-1}")
                userDetailView.showLoadingProgressDialog()

            },
                {

                    Log.e(tag, "getUserDetail : $it")
                    userDetailView.dismissLoadingProgressDialog()
                }, {
                    userDetailView.dismissLoadingProgressDialog()
                }, {

                })
    }
}