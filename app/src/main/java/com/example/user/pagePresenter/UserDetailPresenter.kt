package com.example.user.pagePresenter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.user.MainActivity
import com.example.user.R
import com.example.user.pageInterface.UserDetailView
import com.example.user.server.RetrofitHttp
import com.example.user.utils.NetworkUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class UserDetailPresenter(private val context : Context) {

    private lateinit var userDetailView : UserDetailView
    private var login = ""
    private val tag = javaClass.simpleName

    fun setView(userDetailView: UserDetailView) {

        this.userDetailView = userDetailView
    }

    fun setLogin(intent: Intent){

        login = intent.getStringExtra("login")?:""

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
        service.getApi().getAUser(login)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({

                       //Log.i(tag, "getUserDetail : ${it.code()}")
                       if(it.isSuccessful)
                       {
                           val userDetail = it.body()
                           val avatarUrl = userDetail?.avatar_url?:""
                           val name = userDetail?.name?:""
                           val bio = userDetail?.bio?:""
                           val login = userDetail?.login?:""
                           val siteAdmin = userDetail?.site_admin?:false
                           val location = userDetail?.location?:""
                           val blog = userDetail?.blog?:""

                           userDetailView.loadCircleImage(avatarUrl)
                           userDetailView.setNameText(name)
                           userDetailView.loadBioImage(bio)
                           userDetailView.setLoginText(login)
                           userDetailView.setLocationText(location)
                           userDetailView.setBlogText(blog)

                           val siteAdminVisibility = if(siteAdmin){

                               View.VISIBLE
                           }else
                           {
                               View.INVISIBLE
                           }

                           userDetailView.setSiteAdminVisibility(siteAdminVisibility)
                       }

            },
                {

                    //Log.e(tag, "getUserDetail : $it")
                    userDetailView.dismissLoadingProgressDialog()
                }, {
                    userDetailView.dismissLoadingProgressDialog()
                }, {

                })
    }

    fun loadCircleImage(img: ImageView, url: String){

        if (url.isNotEmpty()) {

            Glide.with(context)
                .load(url)
                .into(img)
        } else {

            userDetailView.setCircleImageResource(R.mipmap.ic_launcher_round)
        }
    }

    fun loadBioImage(img: ImageView, url: String){

        if (url.isNotEmpty()) {

            Glide.with(context)
                .load(url)
                .into(img)
        } else {

            userDetailView.setBioImageResource(R.mipmap.ic_launcher_round)
        }
    }

    fun backOnClick(activity: Activity){

        val i= Intent(context, MainActivity::class.java)
        context.startActivity(i)
        activity.finish()
    }
}