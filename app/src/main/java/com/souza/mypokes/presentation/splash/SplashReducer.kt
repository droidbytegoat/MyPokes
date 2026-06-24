package com.souza.mypokes.presentation.splash

import com.souza.mypokes.presentation.mvi.Reducer

class SplashReducer : Reducer<SplashState, SplashIntent> {
    override fun reduce(state: SplashState, intent: SplashIntent): SplashState = when (intent) {
        SplashIntent.Initialize -> state.copy(isLoading = true)
        SplashIntent.OnReady -> state.copy(isLoading = false)
    }
}
