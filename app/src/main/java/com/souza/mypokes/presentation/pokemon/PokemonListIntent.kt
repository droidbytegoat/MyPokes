package com.souza.mypokes.presentation.pokemon

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.presentation.mvi.UiIntent

sealed interface PokemonListIntent : UiIntent {
    data object LoadInitial : PokemonListIntent
    data object LoadNextPage : PokemonListIntent
    data object Refresh : PokemonListIntent
    data class Search(val query: String) : PokemonListIntent
    data object ClearSearch : PokemonListIntent
    data class OnPokemonClick(val pokemonId: Int) : PokemonListIntent
    data class ToggleFavorite(val pokemon: Pokemon) : PokemonListIntent

    // Internal — dispatched by middleware or ViewModel
    data class PokemonListLoaded(val pokemon: List<Pokemon>) : PokemonListIntent
    data class LoadMoreLoaded(val pokemon: List<Pokemon>) : PokemonListIntent
    data class SearchResultsLoaded(val results: List<Pokemon>) : PokemonListIntent
    data class UpdateFavorites(val favoriteIds: Set<Int>) : PokemonListIntent
    data class LoadFailed(val message: String) : PokemonListIntent
}
