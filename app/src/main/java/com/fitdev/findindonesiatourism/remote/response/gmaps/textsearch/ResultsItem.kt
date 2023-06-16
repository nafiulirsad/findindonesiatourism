package com.fitdev.findindonesiatourism.remote.response.gmaps.textsearch

import com.google.gson.annotations.SerializedName

data class ResultsItem(

	@field:SerializedName("formatted_address")
	val formattedAddress: String? = null,

	@field:SerializedName("types")
	val types: List<String?>? = null,

	@field:SerializedName("business_status")
	val businessStatus: String? = null,

	@field:SerializedName("icon")
	val icon: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("icon_background_color")
	val iconBackgroundColor: String? = null,

	@field:SerializedName("photos")
	val photos: List<PhotosItem?>? = null,

	@field:SerializedName("reference")
	val reference: String? = null,

	@field:SerializedName("user_ratings_total")
	val userRatingsTotal: Int? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("opening_hours")
	val openingHours: OpeningHours? = null,

	@field:SerializedName("geometry")
	val geometry: Geometry? = null,

	@field:SerializedName("icon_mask_base_uri")
	val iconMaskBaseUri: String? = null,

	@field:SerializedName("plus_code")
	val plusCode: PlusCode? = null,

	@field:SerializedName("place_id")
	val placeId: String? = null
)