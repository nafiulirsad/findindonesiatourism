package com.fitdev.findindonesiatourism.remote.response.register

import com.google.gson.annotations.SerializedName

data class UserRegisteredItem(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("phoneNumber")
	val phoneNumber: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)