package com.souza.mypokes.presentation.splash

import com.souza.mypokes.presentation.mvi.BaseViewModel
import com.souza.mypokes.presentation.mvi.Middleware
import com.souza.mypokes.presentation.mvi.Reducer
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor() : BaseViewModel<SplashState, SplashIntent, SplashEffect>(
    initialState = SplashState(),
) {
    override val reducer: Reducer<SplashState, SplashIntent> = SplashReducer()
    override val middlewares: List<Middleware<SplashState, SplashIntent, SplashEffect>> =
        listOf(SplashMiddleware())
}
