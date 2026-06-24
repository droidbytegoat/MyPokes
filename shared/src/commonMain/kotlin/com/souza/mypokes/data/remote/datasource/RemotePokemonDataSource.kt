package com.souza.mypokes.data.remote.datasource

import com.souza.mypokes.data.remote.dto.PokemonDetailDto
import com.souza.mypokes.data.remote.dto.PokemonListResponseDto

interface RemotePokemonDataSource {
    suspend fun getPokemonList(offset: Int, limit: Int): PokemonListResponseDto
    suspend fun getPokemonDetail(id: Int): PokemonDetailDto
    suspend fun searchPokemon(name: String): PokemonDetailDto
}
