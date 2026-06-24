package com.souza.mypokes.presentation.settings

import com.souza.mypokes.data.preferences.AppTheme
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsReducerTest {

    private val reducer = SettingsReducer()
    private val initial = SettingsState()

    // ── Initial state ────────────────────────────────────────────────────────

    @Test
    fun `initial state has isLoading true and FOLLOW_SYSTEM theme`() {
        assertTrue(initial.isLoading)
        assertEquals(AppTheme.FOLLOW_SYSTEM, initial.selectedTheme)
    }

    // ── SettingsLoaded ───────────────────────────────────────────────────────

    @Test
    fun `SettingsLoaded clears loading and sets theme to LIGHT`() {
        val result = reducer.reduce(initial, SettingsIntent.SettingsLoaded(AppTheme.LIGHT))
        assertFalse(result.isLoading)
        assertEquals(AppTheme.LIGHT, result.selectedTheme)
    }

    @Test
    fun `SettingsLoaded clears loading and sets theme to DARK`() {
        val result = reducer.reduce(initial, SettingsIntent.SettingsLoaded(AppTheme.DARK))
        assertFalse(result.isLoading)
        assertEquals(AppTheme.DARK, result.selectedTheme)
    }

    @Test
    fun `SettingsLoaded clears loading and sets theme to FOLLOW_SYSTEM`() {
        val result = reducer.reduce(initial, SettingsIntent.SettingsLoaded(AppTheme.FOLLOW_SYSTEM))
        assertFalse(result.isLoading)
        assertEquals(AppTheme.FOLLOW_SYSTEM, result.selectedTheme)
    }

    // ── SetTheme ─────────────────────────────────────────────────────────────

    @Test
    fun `SetTheme updates selectedTheme to LIGHT`() {
        val ready = initial.copy(isLoading = false, selectedTheme = AppTheme.FOLLOW_SYSTEM)
        val result = reducer.reduce(ready, SettingsIntent.SetTheme(AppTheme.LIGHT))
        assertEquals(AppTheme.LIGHT, result.selectedTheme)
        assertFalse(result.isLoading)
    }

    @Test
    fun `SetTheme updates selectedTheme to DARK`() {
        val ready = initial.copy(isLoading = false, selectedTheme = AppTheme.LIGHT)
        val result = reducer.reduce(ready, SettingsIntent.SetTheme(AppTheme.DARK))
        assertEquals(AppTheme.DARK, result.selectedTheme)
    }

    @Test
    fun `SetTheme back to FOLLOW_SYSTEM from DARK`() {
        val ready = initial.copy(isLoading = false, selectedTheme = AppTheme.DARK)
        val result = reducer.reduce(ready, SettingsIntent.SetTheme(AppTheme.FOLLOW_SYSTEM))
        assertEquals(AppTheme.FOLLOW_SYSTEM, result.selectedTheme)
    }

    @Test
    fun `SetTheme to same value is a no-op on selectedTheme`() {
        val ready = initial.copy(isLoading = false, selectedTheme = AppTheme.DARK)
        val result = reducer.reduce(ready, SettingsIntent.SetTheme(AppTheme.DARK))
        assertEquals(AppTheme.DARK, result.selectedTheme)
    }
}
