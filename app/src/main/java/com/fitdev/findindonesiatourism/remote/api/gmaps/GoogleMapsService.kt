package com.fitdev.findindonesiatourism.remote.api.gmaps

import com.fitdev.findindonesiatourism.remote.response.gmaps.nearbysearch.NearbySearchResponse
import com.fitdev.findindonesiatourism.remote.response.gmaps.placedetails.PlaceDetailsResponse
import com.fitdev.findindonesiatourism.remote.response.gmaps.reversegeocoding.ReverseGeocodingResponse
import com.fitdev.findindonesiatourism.remote.response.gmaps.textsearch.TextSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleMapsService {
    @GET("geocode/json")
    fun getAddressByCoordinate(
        @Query("key") key: String,
        @Query("latlng") latlng: String
    ): Call<ReverseGeocodingResponse>

    @GET("place/nearbysearch/json")
    fun getNearbyPlaceByCoordinate(
        @Query("key") key: String,
        @Query("location") location: String,
        @Query("radius") radius: String,
        @Query("type") type: String,
        @Query("opennow") opennow: String,
    ): Call<NearbySearchResponse>

    @GET("place/textsearch/json")
    fun getSearchPlaceByQuery(
        @Query("key") key: String,
        @Query("query") query: String,
        @Query("opennow") opennow: String,
        @Query("radius") radius: String,
    ): Call<TextSearchResponse>

    @GET("place/details/json")
    fun getPlaceDetails(
        @Query("key") key: String,
        @Query("place_id") place_id: String,
    ): Call<PlaceDetailsResponse>
}