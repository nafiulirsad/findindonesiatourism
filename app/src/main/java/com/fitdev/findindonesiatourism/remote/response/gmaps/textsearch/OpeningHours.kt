package com.fitdev.findindonesiatourism.remote.response.gmaps.textsearch

import com.google.gson.annotations.SerializedName

data class OpeningHours(

	@field:SerializedName("open_now")
	val openNow: Boolean? = null
)