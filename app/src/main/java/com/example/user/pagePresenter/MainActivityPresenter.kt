package com.example.user.pagePresenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import com.example.user.R
import com.example.user.UserDetail
import com.example.user.pageInterface.MainActivityView
import com.example.user.server.RetrofitHttp
import com.example.user.struct.StructUserItem
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

                //Log.i(tag, "getUserList : ${it.code()}, count :${it.body()?.size?:-1}")

                if(it.isSuccessful)
                {
                    val array = it.body()?:ArrayList<StructUserItem>()
                    mainActivityView.setItemTotal(array.size)
                    mainActivityView.updateListView(array)
                }else{
                    val array = ArrayList<StructUserItem>()
                    mainActivityView.setItemTotal(array.size)
                    mainActivityView.updateListView(array)
                }

            },
            {

                //Log.e(tag, "getUserList : $it")
                val array = ArrayList<StructUserItem>()
                mainActivityView.dismissLoadingProgressDialog()
                mainActivityView.setItemTotal(array.size)
                mainActivityView.updateListView(array)

            }, {
                mainActivityView.dismissLoadingProgressDialog()

            }, {

            })
    }

    fun onItemClick(activity: Activity, view: View, position: Int, data: ArrayList<StructUserItem>){

        //Log.i(tag, "position: $position")

        val login = data[position].login
        val i = Intent(context, UserDetail::class.java)
        i.putExtra("login", login)

        context.startActivity(i)
        activity.finish()
    }
}