package com.souza.mypokes.presentation.pokemon

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.presentation.mvi.UiState

data class PokemonListState(
    val pokemon: List<Pokemon> = emptyList(),
    val favoriteIds: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val isSearchActive: Boolean = false,
    val searchResults: List<Pokemon> = emptyList(),
    val currentOffset: Int = 0,
    val hasNextPage: Boolean = true,
    val isRequestInFlight: Boolean = false,
) : UiState {
    val displayList: List<Pokemon> get() = if (isSearchActive) searchResults else pokemon
}
