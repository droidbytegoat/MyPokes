package com.souza.mypokes.domain.usecase

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.repository.PokemonRepository

class GetPokemonListUseCase(private val repository: PokemonRepository) {

    suspend operator fun invoke(offset: Int, limit: Int = PAGE_SIZE): Result<List<Pokemon>> =
        repository.getPokemonList(offset, limit)

    companion object {
        const val PAGE_SIZE = 20
    }
}
