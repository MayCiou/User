package com.example.user.server

import com.example.user.struct.User
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET

interface HttpApi {

    @GET("/users")
    fun getUsers(): Observable<Response<User>>
}