package com.fitdev.findindonesiatourism.remote.response.gmaps.reversegeocoding

import com.google.gson.annotations.SerializedName

data class ReverseGeocodingResponse(

	@field:SerializedName("plus_code")
	val plusCode: PlusCode? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem?>? = null,

	@field:SerializedName("status")
	val status: String? = null
)