package com.souza.mypokes.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.souza.mypokes.domain.usecase.GetFavoritesUseCase
import com.souza.mypokes.domain.usecase.GetPokemonDetailUseCase
import com.souza.mypokes.domain.usecase.ToggleFavoriteUseCase
import com.souza.mypokes.presentation.mvi.BaseViewModel
import com.souza.mypokes.presentation.mvi.Middleware
import com.souza.mypokes.presentation.mvi.Reducer
import com.souza.mypokes.presentation.navigation.Screen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getPokemonDetailUseCase: GetPokemonDetailUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
) : BaseViewModel<PokemonDetailState, PokemonDetailIntent, PokemonDetailEffect>(
    initialState = PokemonDetailState(),
) {
    private val pokemonId: Int = checkNotNull(savedStateHandle[Screen.PokemonDetail.Args.POKEMON_ID])

    override val reducer: Reducer<PokemonDetailState, PokemonDetailIntent> = PokemonDetailReducer()
    override val middlewares: List<Middleware<PokemonDetailState, PokemonDetailIntent, PokemonDetailEffect>> =
        listOf(PokemonDetailMiddleware(getPokemonDetailUseCase, toggleFavoriteUseCase))

    init {
        observeFavorites()
        dispatch(PokemonDetailIntent.LoadDetail(pokemonId))
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase()
                .map { favorites -> favorites.any { it.id == pokemonId } }
                .collect { dispatch(PokemonDetailIntent.FavoriteStatusLoaded(it)) }
        }
    }
}
