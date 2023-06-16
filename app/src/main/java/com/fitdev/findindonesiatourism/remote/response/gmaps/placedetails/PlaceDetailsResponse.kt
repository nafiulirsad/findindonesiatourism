package com.fitdev.findindonesiatourism.remote.response.gmaps.placedetails

import com.google.gson.annotations.SerializedName

data class PlaceDetailsResponse(

	@field:SerializedName("result")
	val result: Result? = null,

	@field:SerializedName("html_attributions")
	val htmlAttributions: List<Any?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)