package com.souza.mypokes.data.repository

import com.souza.mypokes.data.local.datasource.LocalFavoritesDataSource
import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class FavoritesRepositoryImpl(
    private val localDataSource: LocalFavoritesDataSource,
) : FavoritesRepository {

    override fun getFavorites(): Flow<List<Pokemon>> = localDataSource.getFavorites()

    override suspend fun toggleFavorite(pokemon: Pokemon) {
        if (localDataSource.isFavorite(pokemon.id)) {
            localDataSource.removeFavorite(pokemon.id)
        } else {
            localDataSource.addFavorite(pokemon)
        }
    }
}
