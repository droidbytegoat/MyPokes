package com.souza.mypokes.presentation.settings

import com.souza.mypokes.data.preferences.AppTheme
import com.souza.mypokes.presentation.mvi.UiState

data class SettingsState(
    val selectedTheme: AppTheme = AppTheme.FOLLOW_SYSTEM,
    val isLoading: Boolean = true,
) : UiState
