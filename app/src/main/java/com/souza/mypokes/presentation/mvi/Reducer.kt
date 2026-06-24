package com.souza.mypokes.presentation.mvi

fun interface Reducer<S : UiState, I : UiIntent> {
    fun reduce(state: S, intent: I): S
}
