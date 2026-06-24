package com.souza.mypokes.data.repository

import com.souza.mypokes.data.mapper.PokemonMapper
import com.souza.mypokes.data.remote.datasource.RemotePokemonDataSource
import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.model.PokemonDetail
import com.souza.mypokes.domain.repository.PokemonRepository

class PokemonRepositoryImpl(
    private val remoteDataSource: RemotePokemonDataSource,
) : PokemonRepository {

    override suspend fun getPokemonList(offset: Int, limit: Int): Result<List<Pokemon>> =
        runCatching {
            remoteDataSource.getPokemonList(offset, limit).results.map {
                PokemonMapper.mapToPokemonFromEntry(it)
            }
        }

    override suspend fun getPokemonDetail(id: Int): Result<PokemonDetail> =
        runCatching {
            PokemonMapper.mapToDetail(remoteDataSource.getPokemonDetail(id))
        }

    override suspend fun searchPokemon(query: String): Result<List<Pokemon>> =
        runCatching {
            listOf(PokemonMapper.mapToPokemon(remoteDataSource.searchPokemon(query)))
        }
}
