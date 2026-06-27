package com.souza.mypokes.data.repository.fake

import com.souza.mypokes.data.remote.datasource.RemotePokemonDataSource
import com.souza.mypokes.data.remote.dto.PokemonDetailDto
import com.souza.mypokes.data.remote.dto.PokemonEntryDto
import com.souza.mypokes.data.remote.dto.PokemonListResponseDto
import com.souza.mypokes.data.remote.dto.PokemonOfficialArtworkDto
import com.souza.mypokes.data.remote.dto.PokemonOtherSpritesDto
import com.souza.mypokes.data.remote.dto.PokemonSpritesDto
import com.souza.mypokes.data.remote.dto.PokemonStatDto
import com.souza.mypokes.data.remote.dto.PokemonStatSlotDto
import com.souza.mypokes.data.remote.dto.PokemonTypeDto
import com.souza.mypokes.data.remote.dto.PokemonTypeSlotDto

class FakeRemotePokemonDataSource : RemotePokemonDataSource {

    private val entries = mutableListOf<PokemonEntryDto>()
    private val details = mutableMapOf<Int, PokemonDetailDto>()
    var shouldFail = false
    var errorMessage = "Network error"

    override suspend fun getPokemonList(offset: Int, limit: Int): PokemonListResponseDto {
        if (shouldFail) throw Exception(errorMessage)
        return PokemonListResponseDto(
            count = entries.size,
            next = null,
            previous = null,
            results = entries.drop(offset).take(limit),
        )
    }

    override suspend fun getPokemonDetail(id: Int): PokemonDetailDto {
        if (shouldFail) throw Exception(errorMessage)
        return details[id] ?: throw Exception("Pokemon $id not found")
    }

    override suspend fun searchPokemon(name: String): PokemonDetailDto {
        if (shouldFail) throw Exception(errorMessage)
        return details.values.find { it.name == name.lowercase() }
            ?: throw Exception("Pokemon '$name' not found")
    }

    fun addPokemon(id: Int, name: String) {
        val baseUrl = "https://pokeapi.co/api/v2/pokemon/$id/"
        entries.add(PokemonEntryDto(name = name, url = baseUrl))
        details[id] = PokemonDetailDto(
            id = id,
            name = name,
            height = 10,
            weight = 100,
            types = listOf(PokemonTypeSlotDto(slot = 1, type = PokemonTypeDto("normal", "$baseUrl/type/1/"))),
            stats = listOf(PokemonStatSlotDto(baseStat = 50, stat = PokemonStatDto("hp", "$baseUrl/stat/1/"))),
            sprites = PokemonSpritesDto(
                other = PokemonOtherSpritesDto(
                    officialArtwork = PokemonOfficialArtworkDto("https://example.com/$id.png")
                )
            ),
        )
    }
}
