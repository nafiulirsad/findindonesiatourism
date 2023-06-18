package com.fitdev.findindonesiatourism.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(favorite: Favorite)
    @Update
    fun update(favorite: Favorite)
    @Delete
    fun delete(favorite: Favorite)
    @Query("SELECT * from favorite ORDER BY id ASC")
    fun getAllFavorites(): LiveData<List<Favorite>>
    @Query("SELECT * FROM favorite WHERE placeId = :placeId")
    fun getFavoriteByPlaceId(placeId: String): LiveData<Favorite>
}