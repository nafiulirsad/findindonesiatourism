package com.fitdev.findindonesiatourism.remote.api.categories

import com.fitdev.findindonesiatourism.remote.response.categories.CategoriesResponse
import retrofit2.Call
import retrofit2.http.GET

interface TourismCategoriesService {
    @GET("categories.json")
    fun getCategories(): Call<CategoriesResponse>
}