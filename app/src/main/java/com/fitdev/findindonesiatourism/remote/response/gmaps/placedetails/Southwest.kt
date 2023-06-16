package com.fitdev.findindonesiatourism.remote.response.gmaps.placedetails

import com.google.gson.annotations.SerializedName

data class Southwest(

	@field:SerializedName("lng")
	val lng: Any? = null,

	@field:SerializedName("lat")
	val lat: Any? = null
)