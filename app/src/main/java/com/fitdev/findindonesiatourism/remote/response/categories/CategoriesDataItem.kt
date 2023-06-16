package com.fitdev.findindonesiatourism.remote.response.categories

import com.google.gson.annotations.SerializedName

data class CategoriesDataItem(

	@field:SerializedName("categoryImage")
	val categoryImage: String? = null,

	@field:SerializedName("categoryName")
	val categoryName: String? = null
)