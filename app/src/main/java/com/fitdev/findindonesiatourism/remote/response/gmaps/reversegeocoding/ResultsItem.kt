package com.fitdev.findindonesiatourism.remote.response.gmaps.reversegeocoding

import com.google.gson.annotations.SerializedName

data class ResultsItem(

	@field:SerializedName("formatted_address")
	val formattedAddress: String? = null,

	@field:SerializedName("types")
	val types: List<String?>? = null,

	@field:SerializedName("geometry")
	val geometry: Geometry? = null,

	@field:SerializedName("address_components")
	val addressComponents: List<AddressComponentsItem?>? = null,

	@field:SerializedName("place_id")
	val placeId: String? = null
)