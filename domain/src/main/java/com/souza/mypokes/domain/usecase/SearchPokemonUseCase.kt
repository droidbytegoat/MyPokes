package com.souza.mypokes.domain.usecase

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.repository.PokemonRepository

class SearchPokemonUseCase(private val repository: PokemonRepository) {

    suspend operator fun invoke(query: String): Result<List<Pokemon>> =
        repository.searchPokemon(query.trim())
}
