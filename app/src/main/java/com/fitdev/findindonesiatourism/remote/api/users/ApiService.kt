package com.fitdev.findindonesiatourism.remote.api.users

import com.fitdev.findindonesiatourism.remote.response.login.LoginResponse
import com.fitdev.findindonesiatourism.remote.response.register.RegisterResponse
import com.fitdev.findindonesiatourism.remote.response.update.UpdateResponse
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded

interface ApiService {
    @FormUrlEncoded
    @POST("users/register")
    fun register(
        @Field("fullname") fullname: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("phoneNumber") phoneNumber: String,
        @Field("profileImage") profileImage: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("users/login")
    fun login(
        @Field("usernameOrEmail") usernameOrEmail: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("users/update")
    fun update(
        @Field("profileImage") profileImage: String,
        @Field("username") username: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("fullname") fullname: String,
        @Field("phoneNumber") phoneNumber: String
    ) : Call<UpdateResponse>
}