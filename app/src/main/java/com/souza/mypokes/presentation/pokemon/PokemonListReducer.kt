package com.souza.mypokes.presentation.pokemon

import com.souza.mypokes.domain.usecase.GetPokemonListUseCase
import com.souza.mypokes.presentation.mvi.Reducer

class PokemonListReducer : Reducer<PokemonListState, PokemonListIntent> {

    override fun reduce(state: PokemonListState, intent: PokemonListIntent): PokemonListState =
        when (intent) {
            PokemonListIntent.LoadInitial -> state.copy(
                isLoading = true,
                error = null,
                isRequestInFlight = true,
            )
            PokemonListIntent.Refresh -> state.copy(
                isRefreshing = true,
                error = null,
                isRequestInFlight = true,
            )
            PokemonListIntent.LoadNextPage -> when {
                state.isRequestInFlight || !state.hasNextPage -> state
                else -> state.copy(isLoadingMore = true, isRequestInFlight = true)
            }
            is PokemonListIntent.Search -> state.copy(
                searchQuery = intent.query,
                isSearchActive = intent.query.isNotBlank(),
            )
            PokemonListIntent.ClearSearch -> state.copy(
                searchQuery = "",
                isSearchActive = false,
                searchResults = emptyList(),
            )
            is PokemonListIntent.PokemonListLoaded -> state.copy(
                pokemon = intent.pokemon,
                isLoading = false,
                isRefreshing = false,
                isRequestInFlight = false,
                error = null,
                currentOffset = intent.pokemon.size,
                hasNextPage = intent.pokemon.size >= PAGE_SIZE,
            )
            is PokemonListIntent.LoadMoreLoaded -> state.copy(
                pokemon = state.pokemon + intent.pokemon,
                isLoadingMore = false,
                isRequestInFlight = false,
                currentOffset = state.currentOffset + intent.pokemon.size,
                hasNextPage = intent.pokemon.size >= PAGE_SIZE,
            )
            is PokemonListIntent.SearchResultsLoaded -> state.copy(searchResults = intent.results)
            is PokemonListIntent.UpdateFavorites -> state.copy(favoriteIds = intent.favoriteIds)
            is PokemonListIntent.LoadFailed -> state.copy(
                isLoading = false,
                isLoadingMore = false,
                isRefreshing = false,
                isRequestInFlight = false,
                error = intent.message,
            )
            is PokemonListIntent.OnPokemonClick -> state
            is PokemonListIntent.ToggleFavorite -> state
        }

    companion object {
        private val PAGE_SIZE = GetPokemonListUseCase.PAGE_SIZE
    }
}
