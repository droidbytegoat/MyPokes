package com.souza.mypokes.presentation.favorites

import com.souza.mypokes.domain.usecase.ToggleFavoriteUseCase
import com.souza.mypokes.base.Middleware

class FavoritesMiddleware(
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : Middleware<FavoritesState, FavoritesIntent, FavoritesEffect> {

    override suspend fun process(
        intent: FavoritesIntent,
        state: FavoritesState,
        dispatch: (FavoritesIntent) -> Unit,
        sendEffect: (FavoritesEffect) -> Unit,
    ) {
        when (intent) {
            is FavoritesIntent.OnPokemonClick ->
                sendEffect(FavoritesEffect.NavigateToDetail(intent.pokemonId))

            is FavoritesIntent.RemoveFavorite ->
                toggleFavoriteUseCase(intent.pokemon)

            is FavoritesIntent.FavoritesLoaded -> Unit
        }
    }
}
