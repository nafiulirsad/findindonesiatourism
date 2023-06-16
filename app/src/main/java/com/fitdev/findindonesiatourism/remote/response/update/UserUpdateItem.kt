package com.fitdev.findindonesiatourism.remote.response.update

import com.google.gson.annotations.SerializedName

data class UserUpdateItem(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("email")
    val email: String,

    @field:SerializedName("username")
    val username: String,

    @field:SerializedName("password")
    val password: String,

    @field:SerializedName("phoneNumber")
    val phoneNumber: String,

    @field:SerializedName("fullname")
    val fullname: String,

    @field:SerializedName("profileImage")
    val profileImage: String
)