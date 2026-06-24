package com.souza.mypokes.presentation.pokemon

import com.souza.mypokes.presentation.mvi.UiEffect

sealed interface PokemonListEffect : UiEffect {
    data class NavigateToDetail(val pokemonId: Int) : PokemonListEffect
}
