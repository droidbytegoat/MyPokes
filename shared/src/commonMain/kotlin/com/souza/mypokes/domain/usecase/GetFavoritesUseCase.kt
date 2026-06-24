package com.souza.mypokes.domain.usecase

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow

class GetFavoritesUseCase(private val repository: FavoritesRepository) {

    operator fun invoke(): Flow<List<Pokemon>> = repository.getFavorites()
}
