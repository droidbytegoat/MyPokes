package com.souza.mypokes.presentation.detail

import com.souza.mypokes.base.UiEffect

sealed interface PokemonDetailEffect : UiEffect {
    data object NavigateBack : PokemonDetailEffect
}
