package com.souza.mypokes.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemePreferencesDataSource @Inject constructor(
    private val dataStore: DataStore<Preferences>,
) {
    val theme: Flow<AppTheme> = dataStore.data
        .catch { emit(androidx.datastore.preferences.core.emptyPreferences()) }
        .map { preferences ->
            runCatching {
                AppTheme.valueOf(preferences[THEME_KEY] ?: AppTheme.FOLLOW_SYSTEM.name)
            }.getOrDefault(AppTheme.FOLLOW_SYSTEM)
        }

    suspend fun setTheme(theme: AppTheme) {
        dataStore.edit { preferences -> preferences[THEME_KEY] = theme.name }
    }

    companion object {
        private val THEME_KEY = stringPreferencesKey("app_theme")
    }
}
