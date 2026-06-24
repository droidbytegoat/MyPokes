package com.souza.mypokes.presentation.splash

import com.souza.mypokes.presentation.mvi.UiIntent

sealed interface SplashIntent : UiIntent {
    data object Initialize : SplashIntent
    data object OnReady : SplashIntent
}
