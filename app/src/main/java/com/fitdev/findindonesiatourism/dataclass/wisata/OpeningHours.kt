package com.fitdev.findindonesiatourism.dataclass.wisata

import com.google.gson.annotations.SerializedName

data class OpeningHours(

	@field:SerializedName("open_now")
	val openNow: Boolean? = null
)