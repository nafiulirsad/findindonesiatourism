package com.fitdev.findindonesiatourism.dataclass

import com.google.gson.annotations.SerializedName

data class PulauResponseItem(

	@field:SerializedName("nama_foto_region")
	val namaFotoRegion: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("keyword")
	val keyword: String? = null
)