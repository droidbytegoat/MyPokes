package com.souza.mypokes.data.local.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY name ASC")
    fun getAllFavorites(): Flow<List<FavoriteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavoriteEntity)

    @Query("DELETE FROM favorites WHERE id = :pokemonId")
    suspend fun deleteById(pokemonId: Int)

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE id = :pokemonId LIMIT 1)")
    suspend fun isFavorite(pokemonId: Int): Boolean
}
