package com.fitdev.findindonesiatourism.remote.response.gmaps.nearbysearch

import com.google.gson.annotations.SerializedName

data class Viewport(

	@field:SerializedName("southwest")
	val southwest: Southwest? = null,

	@field:SerializedName("northeast")
	val northeast: Northeast? = null
)