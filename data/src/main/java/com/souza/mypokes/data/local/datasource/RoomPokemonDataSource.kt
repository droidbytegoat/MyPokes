package com.souza.mypokes.data.local.datasource

import com.souza.mypokes.data.local.db.PokemonDao
import com.souza.mypokes.data.local.db.toDomain
import com.souza.mypokes.data.local.db.toPokemonEntity
import com.souza.mypokes.domain.model.Pokemon
import javax.inject.Inject

class RoomPokemonDataSource @Inject constructor(
    private val dao: PokemonDao,
) : LocalPokemonDataSource {
    override suspend fun getAll(): List<Pokemon> = dao.getAll().map { it.toDomain() }
    override suspend fun insertAll(pokemon: List<Pokemon>) = dao.insertAll(pokemon.map { it.toPokemonEntity() })
    override suspend fun deleteAll() = dao.deleteAll()
}
