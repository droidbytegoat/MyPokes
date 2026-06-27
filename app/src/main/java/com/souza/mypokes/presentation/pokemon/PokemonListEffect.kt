package com.souza.mypokes.presentation.pokemon

import com.souza.mypokes.base.UiEffect

sealed interface PokemonListEffect : UiEffect {
    data class NavigateToDetail(val pokemonId: Int) : PokemonListEffect
}
