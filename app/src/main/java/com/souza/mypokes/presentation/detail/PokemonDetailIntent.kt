package com.souza.mypokes.presentation.detail

import com.souza.mypokes.domain.model.PokemonDetail
import com.souza.mypokes.presentation.mvi.UiIntent

sealed interface PokemonDetailIntent : UiIntent {
    data class LoadDetail(val pokemonId: Int) : PokemonDetailIntent
    data object ToggleFavorite : PokemonDetailIntent
    data object NavigateBack : PokemonDetailIntent

    // Internal — dispatched by middleware or ViewModel
    data class DetailLoaded(val pokemon: PokemonDetail) : PokemonDetailIntent
    data class FavoriteStatusLoaded(val isFavorite: Boolean) : PokemonDetailIntent
    data class LoadFailed(val message: String) : PokemonDetailIntent
}
