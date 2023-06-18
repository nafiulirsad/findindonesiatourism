package com.fitdev.findindonesiatourism.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.fitdev.findindonesiatourism.database.Favorite
import com.fitdev.findindonesiatourism.repository.FavoriteRepository

class FavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun insert(favorite: Favorite) {
        mFavoriteRepository.insert(favorite)
    }
    fun delete(favorite: Favorite) {
        mFavoriteRepository.delete(favorite)
    }
    fun getAllFavorites(): LiveData<List<Favorite>> = mFavoriteRepository.getAllFavorites()
    fun getFavoriteByPlaceId(placeId: String): LiveData<Favorite> = mFavoriteRepository.getFavoriteByPlaceId(placeId)
}