package com.souza.mypokes.presentation.favorites

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.base.UiIntent

sealed interface FavoritesIntent : UiIntent {
    data class OnPokemonClick(val pokemonId: Int) : FavoritesIntent
    data class RemoveFavorite(val pokemon: Pokemon) : FavoritesIntent

    // Internal — dispatched by ViewModel observer
    data class FavoritesLoaded(val favorites: List<Pokemon>) : FavoritesIntent
}
