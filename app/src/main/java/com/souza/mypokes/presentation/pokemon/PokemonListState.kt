package com.souza.mypokes.presentation.pokemon

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.base.UiState

private const val AUTOCOMPLETE_MAX = 3

data class PokemonListState(
    val pokemon: List<Pokemon> = emptyList(),
    val favoriteIds: Set<Int> = emptySet(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val currentOffset: Int = 0,
    val hasNextPage: Boolean = true,
    val isRequestInFlight: Boolean = false,
) : UiState {

    val isSearchActive: Boolean get() = searchQuery.isNotBlank()

    val displayList: List<Pokemon> get() = when {
        searchQuery.isBlank() -> pokemon
        else -> pokemon.filter { it.name.contains(searchQuery.trim(), ignoreCase = true) }
    }

    val autocompleteItems: List<String> get() {
        if (searchQuery.isBlank()) return emptyList()
        val q = searchQuery.trim()
        val names = pokemon.map { it.name }
        val startsWith = names.filter { it.startsWith(q, ignoreCase = true) }
        val contains = names.filter { !it.startsWith(q, ignoreCase = true) && it.contains(q, ignoreCase = true) }
        return (startsWith + contains).take(AUTOCOMPLETE_MAX)
    }
}
