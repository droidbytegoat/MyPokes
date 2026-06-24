package com.souza.mypokes.presentation.detail

import com.souza.mypokes.presentation.mvi.Reducer

class PokemonDetailReducer : Reducer<PokemonDetailState, PokemonDetailIntent> {

    override fun reduce(state: PokemonDetailState, intent: PokemonDetailIntent): PokemonDetailState =
        when (intent) {
            is PokemonDetailIntent.LoadDetail -> state.copy(
                isLoading = true,
                error = null,
            )
            is PokemonDetailIntent.DetailLoaded -> state.copy(
                pokemon = intent.pokemon,
                isLoading = false,
                error = null,
            )
            is PokemonDetailIntent.FavoriteStatusLoaded -> state.copy(
                isFavorite = intent.isFavorite,
            )
            PokemonDetailIntent.ToggleFavorite -> state.copy(
                isFavorite = !state.isFavorite,
            )
            is PokemonDetailIntent.LoadFailed -> state.copy(
                isLoading = false,
                error = intent.message,
            )
            PokemonDetailIntent.NavigateBack -> state
        }
}
