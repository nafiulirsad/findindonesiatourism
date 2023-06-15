package com.fitdev.findindonesiatourism.dataclass.wisata

import com.google.gson.annotations.SerializedName

data class WisataResponse(

	@field:SerializedName("next_page_token")
	val nextPageToken: String? = null,

	@field:SerializedName("html_attributions")
	val htmlAttributions: List<Any?>? = null,

	@field:SerializedName("results")
	val results: List<ResultsItem>? = null,

	@field:SerializedName("status")
	val status: String? = null
)