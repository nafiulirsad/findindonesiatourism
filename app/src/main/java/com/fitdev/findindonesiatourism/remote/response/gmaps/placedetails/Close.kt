package com.fitdev.findindonesiatourism.remote.response.gmaps.placedetails

import com.google.gson.annotations.SerializedName

data class Close(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("time")
	val time: String? = null,

	@field:SerializedName("day")
	val day: Int? = null
)