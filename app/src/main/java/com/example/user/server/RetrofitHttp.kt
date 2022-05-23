package com.example.user.server

import android.util.Log
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitHttp {

    private var api: HttpApi? = null
    private val url = "https://api.github.com"
    private val tag  = javaClass.simpleName

    fun initClient(): Boolean{

        if(api != null)return true

        try {

            val okHttpClient = OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build()

            val it: Retrofit = Retrofit.Builder()
                .baseUrl(url)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            api = it.create(HttpApi::class.java)
            return true


        }catch (e : Exception){

            return false
        }
    }

    fun destroy(){
        api = null
    }

    fun getApi():HttpApi{

        return api!!
    }

    fun getUserList(){

        Log.i(tag, "getUserList")

    }
}