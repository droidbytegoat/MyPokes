package com.souza.mypokes.data.remote.datasource

import com.souza.mypokes.data.remote.api.PokemonApiClient
import com.souza.mypokes.data.remote.dto.PokemonDetailDto
import com.souza.mypokes.data.remote.dto.PokemonListResponseDto

class RemotePokemonDataSourceImpl(
    private val apiClient: PokemonApiClient,
) : RemotePokemonDataSource {

    override suspend fun getPokemonList(offset: Int, limit: Int): PokemonListResponseDto =
        apiClient.getPokemonList(offset, limit)

    override suspend fun getPokemonDetail(id: Int): PokemonDetailDto =
        apiClient.getPokemonDetail(id)

    override suspend fun searchPokemon(name: String): PokemonDetailDto =
        apiClient.searchPokemon(name)
}
