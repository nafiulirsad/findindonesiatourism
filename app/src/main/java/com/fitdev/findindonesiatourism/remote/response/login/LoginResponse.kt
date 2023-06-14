package com.fitdev.findindonesiatourism.remote.response.login

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("success")
	val success: Boolean? = null,

	@field:SerializedName("userLoggedIn")
	val userLoggedIn: List<UserLoggedInItem>,

	@field:SerializedName("message")
	val message: String? = null
)