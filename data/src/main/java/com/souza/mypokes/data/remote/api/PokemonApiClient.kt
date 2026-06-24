package com.souza.mypokes.data.remote.api

import com.souza.mypokes.data.remote.dto.PokemonDetailDto
import com.souza.mypokes.data.remote.dto.PokemonListResponseDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class PokemonApiClient(private val httpClient: HttpClient) {

    suspend fun getPokemonList(offset: Int, limit: Int): PokemonListResponseDto =
        httpClient.get("$BASE_URL/pokemon") {
            parameter("offset", offset)
            parameter("limit", limit)
        }.body()

    suspend fun getPokemonDetail(id: Int): PokemonDetailDto =
        httpClient.get("$BASE_URL/pokemon/$id").body()

    suspend fun searchPokemon(name: String): PokemonDetailDto =
        httpClient.get("$BASE_URL/pokemon/${name.lowercase()}").body()

    companion object {
        const val BASE_URL = "https://pokeapi.co/api/v2"
    }
}
