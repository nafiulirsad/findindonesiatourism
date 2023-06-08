package com.fitdev.findindonesiatourism.remote.response.login

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Login(
    @field:SerializedName("email")
    val usernameOrEmail: String? = null,

    @field:SerializedName("id")
    val id: String? = null,

    @field:SerializedName("token")
    val token: String? = null
) : Parcelable