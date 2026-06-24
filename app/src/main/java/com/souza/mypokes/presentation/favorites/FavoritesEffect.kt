package com.souza.mypokes.presentation.favorites

import com.souza.mypokes.presentation.mvi.UiEffect

sealed interface FavoritesEffect : UiEffect {
    data class NavigateToDetail(val pokemonId: Int) : FavoritesEffect
}
