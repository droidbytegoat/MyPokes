package com.souza.mypokes.presentation.pokemon

import com.souza.mypokes.domain.usecase.GetPokemonListUseCase
import com.souza.mypokes.domain.usecase.ToggleFavoriteUseCase
import com.souza.mypokes.presentation.mvi.Middleware

class PokemonListMiddleware(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : Middleware<PokemonListState, PokemonListIntent, PokemonListEffect> {

    override suspend fun process(
        intent: PokemonListIntent,
        state: PokemonListState,
        dispatch: (PokemonListIntent) -> Unit,
        sendEffect: (PokemonListEffect) -> Unit,
    ) {
        when (intent) {
            PokemonListIntent.LoadInitial -> fetchPage(offset = 0, dispatch)

            PokemonListIntent.Refresh -> fetchPage(offset = 0, dispatch, forceRefresh = true)

            PokemonListIntent.LoadNextPage -> {
                if (!state.isRequestInFlight && state.hasNextPage && !state.isSearchActive) {
                    fetchMore(state.currentOffset, dispatch)
                }
            }

            is PokemonListIntent.ToggleFavorite -> toggleFavoriteUseCase(intent.pokemon)

            is PokemonListIntent.OnPokemonClick ->
                sendEffect(PokemonListEffect.NavigateToDetail(intent.pokemonId))

            else -> Unit
        }
    }

    private suspend fun fetchPage(
        offset: Int,
        dispatch: (PokemonListIntent) -> Unit,
        forceRefresh: Boolean = false,
    ) {
        getPokemonListUseCase(offset, forceRefresh = forceRefresh).fold(
            onSuccess = { dispatch(PokemonListIntent.PokemonListLoaded(it)) },
            onFailure = { dispatch(PokemonListIntent.LoadFailed(it.message ?: "Failed to load Pokémon")) },
        )
    }

    private suspend fun fetchMore(offset: Int, dispatch: (PokemonListIntent) -> Unit) {
        getPokemonListUseCase(offset).fold(
            onSuccess = { dispatch(PokemonListIntent.LoadMoreLoaded(it)) },
            onFailure = { dispatch(PokemonListIntent.LoadFailed(it.message ?: "Failed to load more")) },
        )
    }
}
