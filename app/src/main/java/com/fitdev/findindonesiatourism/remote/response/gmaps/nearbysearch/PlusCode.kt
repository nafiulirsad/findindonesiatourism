package com.fitdev.findindonesiatourism.remote.response.gmaps.nearbysearch

import com.google.gson.annotations.SerializedName

data class PlusCode(

	@field:SerializedName("compound_code")
	val compoundCode: String? = null,

	@field:SerializedName("global_code")
	val globalCode: String? = null
)