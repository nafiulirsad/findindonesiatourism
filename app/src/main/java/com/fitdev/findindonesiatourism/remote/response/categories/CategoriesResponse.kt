package com.fitdev.findindonesiatourism.remote.response.categories

import com.google.gson.annotations.SerializedName

data class CategoriesResponse(

	@field:SerializedName("categoriesData")
	val categoriesData: List<CategoriesDataItem?>? = null,

	@field:SerializedName("title")
	val title: String? = null
)