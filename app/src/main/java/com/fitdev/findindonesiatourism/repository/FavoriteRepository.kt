package com.fitdev.findindonesiatourism.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.fitdev.findindonesiatourism.database.Favorite
import com.fitdev.findindonesiatourism.database.FavoriteDao
import com.fitdev.findindonesiatourism.database.FavoriteRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoritesDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteRoomDatabase.getDatabase(application)
        mFavoritesDao = db.favoriteDao()
    }
    fun getAllFavorites(): LiveData<List<Favorite>> = mFavoritesDao.getAllFavorites()
    fun getFavoriteByPlaceId(placeId: String): LiveData<Favorite> = mFavoritesDao.getFavoriteByPlaceId(placeId)
    fun insert(favorite: Favorite) {
        executorService.execute { mFavoritesDao.insert(favorite) }
    }
    fun delete(favorite: Favorite) {
        executorService.execute { mFavoritesDao.delete(favorite) }
    }
}