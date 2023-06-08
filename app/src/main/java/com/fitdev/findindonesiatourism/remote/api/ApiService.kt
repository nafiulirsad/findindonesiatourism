package com.fitdev.findindonesiatourism.remote.api

import com.fitdev.findindonesiatourism.remote.response.login.LoginResponse
import com.fitdev.findindonesiatourism.remote.response.register.RegisterResponse
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phoneNumber") phoneNumber: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("usernameOrEmail") usernameOrEmail: String,
        @Field("password") password: String
    ): Call<LoginResponse>
}