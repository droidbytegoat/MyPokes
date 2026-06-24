package com.souza.mypokes.presentation.pokemon

import androidx.lifecycle.viewModelScope
import com.souza.mypokes.domain.usecase.GetFavoritesUseCase
import com.souza.mypokes.domain.usecase.GetPokemonListUseCase
import com.souza.mypokes.domain.usecase.SearchPokemonUseCase
import com.souza.mypokes.domain.usecase.ToggleFavoriteUseCase
import com.souza.mypokes.presentation.mvi.BaseViewModel
import com.souza.mypokes.presentation.mvi.Middleware
import com.souza.mypokes.presentation.mvi.Reducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PokemonListViewModel @Inject constructor(
    private val getPokemonListUseCase: GetPokemonListUseCase,
    private val searchPokemonUseCase: SearchPokemonUseCase,
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
        observeSearchQuery()
        dispatch(PokemonListIntent.LoadInitial)
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase()
                .map { favorites -> favorites.map { it.id }.toSet() }
                .collect { dispatch(PokemonListIntent.UpdateFavorites(it)) }
        }
    }

    private fun observeSearchQuery() {
        viewModelScope.launch {
            state
                .map { it.searchQuery }
                .distinctUntilChanged()
                .debounce(SEARCH_DEBOUNCE_MS)
                .collect { query ->
                    if (query.isBlank()) {
                        dispatch(PokemonListIntent.SearchResultsLoaded(emptyList()))
                    } else {
                        searchPokemonUseCase(query).fold(
                            onSuccess = { dispatch(PokemonListIntent.SearchResultsLoaded(it)) },
                            onFailure = { dispatch(PokemonListIntent.LoadFailed(it.message ?: "Search failed")) },
                        )
                    }
                }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_MS = 300L
    }
}
