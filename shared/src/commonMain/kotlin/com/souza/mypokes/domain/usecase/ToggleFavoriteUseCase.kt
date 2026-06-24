package com.souza.mypokes.domain.usecase

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.repository.FavoritesRepository

class ToggleFavoriteUseCase(private val repository: FavoritesRepository) {

    suspend operator fun invoke(pokemon: Pokemon) = repository.toggleFavorite(pokemon)
}
