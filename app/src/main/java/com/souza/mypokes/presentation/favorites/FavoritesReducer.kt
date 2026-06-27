package com.souza.mypokes.presentation.favorites

import com.souza.mypokes.base.Reducer

class FavoritesReducer : Reducer<FavoritesState, FavoritesIntent> {

    override fun reduce(state: FavoritesState, intent: FavoritesIntent): FavoritesState =
        when (intent) {
            is FavoritesIntent.FavoritesLoaded -> state.copy(
                favorites = intent.favorites,
                isLoading = false,
            )
            is FavoritesIntent.OnPokemonClick -> state
            is FavoritesIntent.RemoveFavorite -> state
        }
}
