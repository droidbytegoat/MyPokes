package com.souza.mypokes.domain.usecase.fake

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.model.PokemonDetail
import com.souza.mypokes.domain.model.PokemonStat
import com.souza.mypokes.domain.model.PokemonType
import com.souza.mypokes.domain.repository.PokemonRepository

class FakePokemonRepository : PokemonRepository {

    val pokemonList = mutableListOf<Pokemon>()
    val detailMap = mutableMapOf<Int, PokemonDetail>()
    var shouldFail = false
    var errorMessage = "Repository error"

    override suspend fun getPokemonList(offset: Int, limit: Int, forceRefresh: Boolean): Result<List<Pokemon>> {
        if (shouldFail) return Result.failure(Exception(errorMessage))
        return Result.success(pokemonList.drop(offset).take(limit))
    }

    override suspend fun getPokemonDetail(id: Int): Result<PokemonDetail> {
        if (shouldFail) return Result.failure(Exception(errorMessage))
        val detail = detailMap[id] ?: return Result.failure(Exception("Pokemon $id not found"))
        return Result.success(detail)
    }

    override suspend fun searchPokemon(query: String): Result<List<Pokemon>> {
        if (shouldFail) return Result.failure(Exception(errorMessage))
        return Result.success(pokemonList.filter { it.name.contains(query, ignoreCase = true) })
    }

    fun addPokemon(vararg pokemon: Pokemon) {
        pokemonList.addAll(pokemon)
        pokemon.forEach { p ->
            detailMap[p.id] = PokemonDetail(
                id = p.id,
                name = p.name,
                imageUrl = p.imageUrl,
                types = listOf(PokemonType("normal", 1)),
                height = 10,
                weight = 100,
                stats = listOf(PokemonStat("hp", 50)),
            )
        }
    }
}
