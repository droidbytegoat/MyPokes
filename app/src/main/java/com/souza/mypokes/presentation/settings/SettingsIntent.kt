package com.souza.mypokes.presentation.settings

import com.souza.mypokes.data.preferences.AppTheme
import com.souza.mypokes.presentation.mvi.UiIntent

sealed interface SettingsIntent : UiIntent {
    data class SetTheme(val theme: AppTheme) : SettingsIntent

    // Internal — dispatched by ViewModel observer
    data class SettingsLoaded(val theme: AppTheme) : SettingsIntent
}
