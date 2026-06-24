package com.souza.mypokes.data.local.datasource

import com.souza.mypokes.data.local.db.FavoriteDao
import com.souza.mypokes.data.local.db.toDomain
import com.souza.mypokes.data.local.db.toEntity
import com.souza.mypokes.domain.model.Pokemon
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RoomFavoritesDataSource @Inject constructor(
    private val dao: FavoriteDao,
) : LocalFavoritesDataSource {

    override fun getFavorites(): Flow<List<Pokemon>> =
        dao.getAllFavorites().map { entities -> entities.map { it.toDomain() } }

    override suspend fun addFavorite(pokemon: Pokemon) = dao.insert(pokemon.toEntity())

    override suspend fun removeFavorite(pokemonId: Int) = dao.deleteById(pokemonId)

    override suspend fun isFavorite(pokemonId: Int): Boolean = dao.isFavorite(pokemonId)
}
