package com.souza.mypokes.domain.usecase

import com.souza.mypokes.domain.model.PokemonDetail
import com.souza.mypokes.domain.repository.PokemonRepository

class GetPokemonDetailUseCase(private val repository: PokemonRepository) {

    suspend operator fun invoke(id: Int): Result<PokemonDetail> =
        repository.getPokemonDetail(id)
}
