package com.fitdev.findindonesiatourism.remote.response.update

import com.google.gson.annotations.SerializedName

data class UpdateResponse(
    @field:SerializedName("success")
    val success: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("userUpdated")
    val userUpdated: List<UserUpdateItem>
)