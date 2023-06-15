package com.fitdev.findindonesiatourism.dataclass.wisata

import com.google.gson.annotations.SerializedName

data class PlusCode(

	@field:SerializedName("compound_code")
	val compoundCode: String? = null,

	@field:SerializedName("global_code")
	val globalCode: String? = null
)