package com.souza.mypokes.base

fun interface Reducer<S : UiState, I : UiIntent> {
    fun reduce(state: S, intent: I): S
}
