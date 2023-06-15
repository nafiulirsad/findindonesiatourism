package com.fitdev.findindonesiatourism.dataclass.popular

import com.google.gson.annotations.SerializedName

data class PopularResponse(

	@field:SerializedName("popularAttractions")
	val popularAttractions: List<PopularAttractionsItem>
)