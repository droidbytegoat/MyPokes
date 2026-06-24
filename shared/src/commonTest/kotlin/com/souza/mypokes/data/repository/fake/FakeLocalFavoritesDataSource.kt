package com.souza.mypokes.data.repository.fake

import com.souza.mypokes.data.local.datasource.LocalFavoritesDataSource
import com.souza.mypokes.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeLocalFavoritesDataSource : LocalFavoritesDataSource {

    private val _favorites = MutableStateFlow<List<Pokemon>>(emptyList())

    override fun getFavorites(): Flow<List<Pokemon>> = _favorites.asStateFlow()

    override suspend fun addFavorite(pokemon: Pokemon) {
        _favorites.value = _favorites.value + pokemon
    }

    override suspend fun removeFavorite(pokemonId: Int) {
        _favorites.value = _favorites.value.filter { it.id != pokemonId }
    }

    override suspend fun isFavorite(pokemonId: Int): Boolean =
        _favorites.value.any { it.id == pokemonId }

    fun setFavorites(vararg pokemon: Pokemon) {
        _favorites.value = pokemon.toList()
    }
}
