package com.souza.mypokes.domain.repository

import com.souza.mypokes.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun getFavorites(): Flow<List<Pokemon>>
    suspend fun toggleFavorite(pokemon: Pokemon)
}
