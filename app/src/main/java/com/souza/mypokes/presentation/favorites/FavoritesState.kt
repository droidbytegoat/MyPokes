package com.souza.mypokes.presentation.favorites

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.base.UiState

data class FavoritesState(
    val favorites: List<Pokemon> = emptyList(),
    val isLoading: Boolean = true,
) : UiState
