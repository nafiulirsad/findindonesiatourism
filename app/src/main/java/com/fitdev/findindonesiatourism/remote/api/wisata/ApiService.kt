package com.fitdev.findindonesiatourism.remote.api.wisata

import com.fitdev.findindonesiatourism.dataclass.PulauResponseItem
import com.fitdev.findindonesiatourism.dataclass.popular.PopularResponse
import com.fitdev.findindonesiatourism.dataclass.wisata.WisataResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("users/list")
    fun getAllPulau(): Call<List<PulauResponseItem>>

    @GET("users/get")
    fun getAllWisata(
        @Query("query") query: String
    ): Call<WisataResponse>
}