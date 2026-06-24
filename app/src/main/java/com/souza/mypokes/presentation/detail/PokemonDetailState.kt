package com.souza.mypokes.presentation.detail

import com.souza.mypokes.domain.model.PokemonDetail
import com.souza.mypokes.presentation.mvi.UiState

data class PokemonDetailState(
    val pokemon: PokemonDetail? = null,
    val isFavorite: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null,
) : UiState
