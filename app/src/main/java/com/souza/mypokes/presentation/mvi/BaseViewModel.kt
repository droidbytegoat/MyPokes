package com.souza.mypokes.presentation.mvi

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : UiState, I : UiIntent, E : UiEffect>(
    initialState: S,
) : ViewModel() {

    protected abstract val reducer: Reducer<S, I>
    protected open val middlewares: List<Middleware<S, I, E>> = emptyList()

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    private val _effects = Channel<E>(Channel.BUFFERED)
    val effects: Flow<E> = _effects.receiveAsFlow()

    fun dispatch(intent: I) {
        val previousState = _state.value
        _state.update { reducer.reduce(it, intent) }
        viewModelScope.launch {
            middlewares.forEach { middleware ->
                launch {
                    middleware.process(
                        intent = intent,
                        state = previousState,
                        dispatch = ::dispatch,
                        sendEffect = ::sendEffect,
                    )
                }
            }
        }
    }

    protected fun sendEffect(effect: E) {
        viewModelScope.launch { _effects.send(effect) }
    }
}
