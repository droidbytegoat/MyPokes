package com.souza.mypokes.data.repository

import com.souza.mypokes.data.local.datasource.LocalPokemonDataSource
import com.souza.mypokes.data.mapper.PokemonMapper
import com.souza.mypokes.data.remote.datasource.RemotePokemonDataSource
import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.model.PokemonDetail
import com.souza.mypokes.domain.repository.PokemonRepository

class PokemonRepositoryImpl(
    private val remoteDataSource: RemotePokemonDataSource,
    private val localDataSource: LocalPokemonDataSource,
) : PokemonRepository {

    override suspend fun getPokemonList(
        offset: Int,
        limit: Int,
        forceRefresh: Boolean,
    ): Result<List<Pokemon>> = runCatching {
        if (offset == 0 && !forceRefresh) {
            val cached = localDataSource.getAll()
            if (cached.isNotEmpty()) return@runCatching cached
        }
        val pokemon = remoteDataSource.getPokemonList(offset, limit).results
            .map { PokemonMapper.mapToPokemonFromEntry(it) }
        if (offset == 0) localDataSource.deleteAll()
        localDataSource.insertAll(pokemon)
        pokemon
    }

    override suspend fun getPokemonDetail(id: Int): Result<PokemonDetail> =
        runCatching { PokemonMapper.mapToDetail(remoteDataSource.getPokemonDetail(id)) }

    override suspend fun searchPokemon(query: String): Result<List<Pokemon>> =
        runCatching { listOf(PokemonMapper.mapToPokemon(remoteDataSource.searchPokemon(query))) }
}
