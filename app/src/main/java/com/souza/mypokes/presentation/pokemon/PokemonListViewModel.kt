package com.souza.mypokes.presentation.pokemon

import androidx.lifecycle.viewModelScope
import com.souza.mypokes.domain.usecase.GetFavoritesUseCase
import com.souza.mypokes.domain.usecase.GetPokemonListUseCase
import com.souza.mypokes.domain.usecase.ToggleFavoriteUseCase
import com.souza.mypokes.presentation.mvi.BaseViewModel
import com.souza.mypokes.presentation.mvi.Middleware
import com.souza.mypokes.presentation.mvi.Reducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
    private val getFavoritesUseCase: GetFavoritesUseCase,
) : BaseViewModel<PokemonListState, PokemonListIntent, PokemonListEffect>(
    initialState = PokemonListState(),
) {
    override val reducer: Reducer<PokemonListState, PokemonListIntent> = PokemonListReducer()
    override val middlewares: List<Middleware<PokemonListState, PokemonListIntent, PokemonListEffect>> =
        listOf(PokemonListMiddleware(getPokemonListUseCase, toggleFavoriteUseCase))

    init {
        observeFavorites()
        dispatch(PokemonListIntent.LoadInitial)
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase()
                .map { favorites -> favorites.map { it.id }.toSet() }
                .collect { dispatch(PokemonListIntent.UpdateFavorites(it)) }
        }
    }
}
