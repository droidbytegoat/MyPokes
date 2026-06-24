package com.souza.mypokes.presentation.splash

import com.souza.mypokes.presentation.mvi.UiState

data class SplashState(
    val isLoading: Boolean = true,
) : UiState
