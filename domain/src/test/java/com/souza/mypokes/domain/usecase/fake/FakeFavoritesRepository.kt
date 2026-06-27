package com.souza.mypokes.domain.usecase.fake

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FakeFavoritesRepository : FavoritesRepository {

    private val _favorites = MutableStateFlow<List<Pokemon>>(emptyList())

    override fun getFavorites(): Flow<List<Pokemon>> = _favorites.asStateFlow()

    override suspend fun toggleFavorite(pokemon: Pokemon) {
        val current = _favorites.value
        _favorites.value = if (current.any { it.id == pokemon.id }) {
            current.filter { it.id != pokemon.id }
        } else {
            current + pokemon
        }
    }

    fun setFavorites(vararg pokemon: Pokemon) {
        _favorites.value = pokemon.toList()
    }
}
