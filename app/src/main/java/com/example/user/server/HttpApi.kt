package com.example.user.server

import com.example.user.struct.StructUser
import com.example.user.struct.StructUserDetail
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface HttpApi {

    @GET("/users")
    fun getUsers(@Query("since") since: Int,@Query("per_page") per_page: Int): Observable<Response<StructUser>>

}