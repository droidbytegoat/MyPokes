package com.souza.mypokes.presentation.settings

import androidx.lifecycle.viewModelScope
import com.souza.mypokes.data.preferences.ThemePreferencesDataSource
import com.souza.mypokes.base.BaseViewModel
import com.souza.mypokes.base.Middleware
import com.souza.mypokes.base.Reducer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themePreferencesDataSource: ThemePreferencesDataSource,
) : BaseViewModel<SettingsState, SettingsIntent, SettingsEffect>(
    initialState = SettingsState(),
) {
    override val reducer: Reducer<SettingsState, SettingsIntent> = SettingsReducer()
    override val middlewares: List<Middleware<SettingsState, SettingsIntent, SettingsEffect>> =
        listOf(SettingsMiddleware(themePreferencesDataSource))

    init {
        observeTheme()
    }

    private fun observeTheme() {
        viewModelScope.launch {
            themePreferencesDataSource.theme.collect { theme ->
                dispatch(SettingsIntent.SettingsLoaded(theme))
            }
        }
    }
}
