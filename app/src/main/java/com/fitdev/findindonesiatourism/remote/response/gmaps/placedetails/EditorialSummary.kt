package com.fitdev.findindonesiatourism.remote.response.gmaps.placedetails

import com.google.gson.annotations.SerializedName

data class EditorialSummary(

	@field:SerializedName("overview")
	val overview: String? = null,

	@field:SerializedName("language")
	val language: String? = null
)