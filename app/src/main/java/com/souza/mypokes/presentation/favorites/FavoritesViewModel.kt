package com.souza.mypokes.presentation.favorites

import androidx.lifecycle.viewModelScope
import com.souza.mypokes.domain.usecase.GetFavoritesUseCase
import com.souza.mypokes.domain.usecase.ToggleFavoriteUseCase
import com.souza.mypokes.base.BaseViewModel
import com.souza.mypokes.base.Middleware
import com.souza.mypokes.base.Reducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoritesUseCase: GetFavoritesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase,
) : BaseViewModel<FavoritesState, FavoritesIntent, FavoritesEffect>(
    initialState = FavoritesState(),
) {
    override val reducer: Reducer<FavoritesState, FavoritesIntent> = FavoritesReducer()
    override val middlewares: List<Middleware<FavoritesState, FavoritesIntent, FavoritesEffect>> =
        listOf(FavoritesMiddleware(toggleFavoriteUseCase))

    init {
        observeFavorites()
    }

    private fun observeFavorites() {
        viewModelScope.launch {
            getFavoritesUseCase().collect { favorites ->
                dispatch(FavoritesIntent.FavoritesLoaded(favorites))
            }
        }
    }
}
