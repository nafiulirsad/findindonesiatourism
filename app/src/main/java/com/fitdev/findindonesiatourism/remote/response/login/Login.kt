package com.fitdev.findindonesiatourism.remote.response.login

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Login(
    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("username")
    val username: String? = null,

    @field:SerializedName("token")
    val token: String? = null,
) : Parcelable