package com.fitdev.findindonesiatourism.dataclass.popular

import com.google.gson.annotations.SerializedName

data class PopularAttractionsItem(

	@field:SerializedName("photoUrl")
	val photoUrl: String,

	@field:SerializedName("placeId")
	val placeId: String,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("rating")
	val rating: Any
)