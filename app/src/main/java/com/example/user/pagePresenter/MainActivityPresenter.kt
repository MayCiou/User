package com.example.user.pagePresenter

import android.app.Activity
import android.content.Context
import android.content.Intent
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
    private var userArray = ArrayList<StructUserItem?>()
    private var lastUserId = 0
    private val perLoad = 20
    private val loadMax = 100
    private val tag = javaClass.simpleName

    fun setView(mainActivityView: MainActivityView) {

        this.mainActivityView = mainActivityView
    }

    fun loadData(service: RetrofitHttp, isOnTouch : Boolean){

        if(isOnTouch)
        {
            if(userArray.size != loadMax)
            {
                getUserList(service, isOnTouch)
            }

        }else
        {
            if(userArray.isEmpty())
            {
                getUserList(service, isOnTouch)
            }
        }

    }

    fun clearUserArray(){

        this.userArray.clear()
    }

    private fun getUserList(service: RetrofitHttp, isOnTouch : Boolean){

        if(!NetworkUtil.isNetworkConnected(context))
        {
            mainActivityView.showAlert(
                context.getString(R.string.message),
                context.getString(R.string.no_network_msg)){}
            return
        }

        if(!service.initClient())return

        if(isOnTouch)
        {
            mainActivityView.addLoadingView()
        }else
        {
            mainActivityView.showLoadingProgressDialog()
        }

        val newAddUserArray = ArrayList<StructUserItem?>()

        service.getApi().getUsers(lastUserId, perLoad)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                //Log.i(tag, "getUserList : ${it.code()}, count :${it.body()?.size?:-1}")

                if(it.isSuccessful)
                {
                    val array = it.body()
                    if(array != null)
                    {
                        lastUserId = array[array.size - 1]?.id?:0
                        newAddUserArray.addAll(array)
                        userArray.addAll(newAddUserArray)
                    }

                }
            },
            {

                //Log.e(tag, "getUserList : $it")
                if(isOnTouch)
                {
                    mainActivityView.removeLoadingView()
                    mainActivityView.setLoaded()
                }else
                {
                    mainActivityView.dismissLoadingProgressDialog()
                }
                newAddUserArray.clear()

            }, {
                 if(isOnTouch)
                 {
                     mainActivityView.removeLoadingView()
                     mainActivityView.updateListView(newAddUserArray)
                     mainActivityView.setItemTotal(userArray.size)
                     mainActivityView.setLoaded()
                 }else
                 {
                     mainActivityView.dismissLoadingProgressDialog()
                     mainActivityView.updateListView(newAddUserArray)
                     mainActivityView.setItemTotal(userArray.size)

                 }

                    newAddUserArray.clear()

            }, {

            })
    }

    fun onItemClick(activity: Activity, view: View, position: Int, data: ArrayList<StructUserItem?>){

        //Log.i(tag, "position: $position")

        val login = data[position]?.login?:return
        val i = Intent(context, UserDetail::class.java)
        i.putExtra("login", login)

        context.startActivity(i)
        activity.finish()
    }
}