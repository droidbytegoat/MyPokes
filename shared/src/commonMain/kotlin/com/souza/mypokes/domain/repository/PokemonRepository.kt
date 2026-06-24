package com.souza.mypokes.domain.repository

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.model.PokemonDetail

interface PokemonRepository {
    suspend fun getPokemonList(offset: Int, limit: Int): Result<List<Pokemon>>
    suspend fun getPokemonDetail(id: Int): Result<PokemonDetail>
    suspend fun searchPokemon(query: String): Result<List<Pokemon>>
}
