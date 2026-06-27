package com.souza.mypokes.presentation.favorites

import com.souza.mypokes.base.UiEffect

sealed interface FavoritesEffect : UiEffect {
    data class NavigateToDetail(val pokemonId: Int) : FavoritesEffect
}
