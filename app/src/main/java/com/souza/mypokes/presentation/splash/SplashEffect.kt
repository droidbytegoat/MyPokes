package com.souza.mypokes.presentation.splash

import com.souza.mypokes.presentation.mvi.UiEffect

sealed interface SplashEffect : UiEffect {
    data object NavigateToMain : SplashEffect
}
