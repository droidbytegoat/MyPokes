package com.souza.mypokes.presentation.detail

import com.souza.mypokes.presentation.mvi.UiEffect

sealed interface PokemonDetailEffect : UiEffect {
    data object NavigateBack : PokemonDetailEffect
}
