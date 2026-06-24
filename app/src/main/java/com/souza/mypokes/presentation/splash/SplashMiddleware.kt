package com.souza.mypokes.presentation.splash

import com.souza.mypokes.presentation.mvi.Middleware
import kotlinx.coroutines.delay

class SplashMiddleware : Middleware<SplashState, SplashIntent, SplashEffect> {

    override suspend fun process(
        intent: SplashIntent,
        state: SplashState,
        dispatch: (SplashIntent) -> Unit,
        sendEffect: (SplashEffect) -> Unit,
    ) {
        if (intent is SplashIntent.Initialize) {
            delay(SPLASH_DELAY_MS)
            dispatch(SplashIntent.OnReady)
            sendEffect(SplashEffect.NavigateToMain)
        }
    }

    companion object {
        private const val SPLASH_DELAY_MS = 1_500L
    }
}
