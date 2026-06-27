package com.souza.mypokes.presentation.settings

import com.souza.mypokes.base.Reducer

class SettingsReducer : Reducer<SettingsState, SettingsIntent> {

    override fun reduce(state: SettingsState, intent: SettingsIntent): SettingsState =
        when (intent) {
            is SettingsIntent.SettingsLoaded -> state.copy(
                selectedTheme = intent.theme,
                isLoading = false,
            )
            is SettingsIntent.SetTheme -> state.copy(
                selectedTheme = intent.theme,
            )
        }
}
