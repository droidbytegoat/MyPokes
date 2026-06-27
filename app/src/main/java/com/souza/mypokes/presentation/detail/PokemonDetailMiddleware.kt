package com.souza.mypokes.presentation.detail

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.usecase.GetPokemonDetailUseCase
import com.souza.mypokes.domain.usecase.ToggleFavoriteUseCase
import com.souza.mypokes.presentation.mvi.Middleware

class PokemonDetailMiddleware(
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : Middleware<PokemonDetailState, PokemonDetailIntent, PokemonDetailEffect> {

    override suspend fun process(
        intent: PokemonDetailIntent,
        state: PokemonDetailState,
        dispatch: (PokemonDetailIntent) -> Unit,
        sendEffect: (PokemonDetailEffect) -> Unit,
    ) {
        when (intent) {
            is PokemonDetailIntent.LoadDetail -> {
                getPokemonDetailUseCase(intent.pokemonId).fold(
                    onSuccess = { dispatch(PokemonDetailIntent.DetailLoaded(it)) },
                    onFailure = { dispatch(PokemonDetailIntent.LoadFailed(it.message ?: "Failed to load Pokémon")) },
                )
            }
            PokemonDetailIntent.ToggleFavorite -> {
                state.pokemon?.let { detail ->
                    val pokemon = Pokemon(
                        id = detail.id,
                        name = detail.name,
                        imageUrl = detail.imageUrl,
                    )
                    toggleFavoriteUseCase(pokemon)
                }
            }
            PokemonDetailIntent.NavigateBack -> sendEffect(PokemonDetailEffect.NavigateBack)
            else -> Unit
        }
    }
}
