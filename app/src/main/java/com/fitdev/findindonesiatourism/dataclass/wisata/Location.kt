package com.fitdev.findindonesiatourism.dataclass.wisata

import com.google.gson.annotations.SerializedName

data class Location(

	@field:SerializedName("lng")
	val lng: Any? = null,

	@field:SerializedName("lat")
	val lat: Any? = null
)