package com.souza.mypokes.base

interface Middleware<S : UiState, I : UiIntent, E : UiEffect> {
    suspend fun process(
        intent: I,
        state: S,
        dispatch: (I) -> Unit,
        sendEffect: (E) -> Unit,
    )
}
