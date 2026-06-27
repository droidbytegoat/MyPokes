package com.souza.mypokes.presentation.settings

import com.souza.mypokes.data.preferences.ThemePreferencesDataSource
import com.souza.mypokes.base.Middleware

class SettingsMiddleware(
    private val themePreferencesDataSource: ThemePreferencesDataSource,
) : Middleware<SettingsState, SettingsIntent, SettingsEffect> {

    override suspend fun process(
        intent: SettingsIntent,
        state: SettingsState,
        dispatch: (SettingsIntent) -> Unit,
        sendEffect: (SettingsEffect) -> Unit,
    ) {
        when (intent) {
            is SettingsIntent.SetTheme -> themePreferencesDataSource.setTheme(intent.theme)
            is SettingsIntent.SettingsLoaded -> Unit
        }
    }
}
