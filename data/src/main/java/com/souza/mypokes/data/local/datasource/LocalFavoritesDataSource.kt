package com.souza.mypokes.data.local.datasource

import com.souza.mypokes.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface LocalFavoritesDataSource {
    fun getFavorites(): Flow<List<Pokemon>>
    suspend fun addFavorite(pokemon: Pokemon)
    suspend fun removeFavorite(pokemonId: Int)
    suspend fun isFavorite(pokemonId: Int): Boolean
}
