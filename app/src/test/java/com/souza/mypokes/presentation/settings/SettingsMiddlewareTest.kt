package com.souza.mypokes.presentation.settings

import com.souza.mypokes.data.preferences.AppTheme
import com.souza.mypokes.data.preferences.ThemePreferencesDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

class SettingsMiddlewareTest {

    private val themePreferencesDataSource: ThemePreferencesDataSource = mockk(relaxed = true)

    private lateinit var middleware: SettingsMiddleware

    private val dispatched = mutableListOf<SettingsIntent>()
    private val effects = mutableListOf<SettingsEffect>()

    private val dispatch: (SettingsIntent) -> Unit = { dispatched.add(it) }
    private val sendEffect: (SettingsEffect) -> Unit = { effects.add(it) }

    @Before
    fun setup() {
        middleware = SettingsMiddleware(themePreferencesDataSource)
        dispatched.clear()
        effects.clear()
    }

    // ── SetTheme ─────────────────────────────────────────────────────────────

    @Test
    fun `SetTheme LIGHT calls setTheme with LIGHT`() = runTest {
        coEvery { themePreferencesDataSource.setTheme(AppTheme.LIGHT) } returns Unit

        middleware.process(SettingsIntent.SetTheme(AppTheme.LIGHT), SettingsState(), dispatch, sendEffect)

        coVerify(exactly = 1) { themePreferencesDataSource.setTheme(AppTheme.LIGHT) }
        assertTrue(dispatched.isEmpty())
        assertTrue(effects.isEmpty())
    }

    @Test
    fun `SetTheme DARK calls setTheme with DARK`() = runTest {
        coEvery { themePreferencesDataSource.setTheme(AppTheme.DARK) } returns Unit

        middleware.process(SettingsIntent.SetTheme(AppTheme.DARK), SettingsState(), dispatch, sendEffect)

        coVerify(exactly = 1) { themePreferencesDataSource.setTheme(AppTheme.DARK) }
        assertTrue(dispatched.isEmpty())
        assertTrue(effects.isEmpty())
    }

    @Test
    fun `SetTheme FOLLOW_SYSTEM calls setTheme with FOLLOW_SYSTEM`() = runTest {
        coEvery { themePreferencesDataSource.setTheme(AppTheme.FOLLOW_SYSTEM) } returns Unit

        middleware.process(SettingsIntent.SetTheme(AppTheme.FOLLOW_SYSTEM), SettingsState(), dispatch, sendEffect)

        coVerify(exactly = 1) { themePreferencesDataSource.setTheme(AppTheme.FOLLOW_SYSTEM) }
        assertTrue(dispatched.isEmpty())
        assertTrue(effects.isEmpty())
    }

    // ── SettingsLoaded ───────────────────────────────────────────────────────

    @Test
    fun `SettingsLoaded produces no side effects`() = runTest {
        middleware.process(
            SettingsIntent.SettingsLoaded(AppTheme.DARK),
            SettingsState(),
            dispatch,
            sendEffect,
        )

        coVerify(exactly = 0) { themePreferencesDataSource.setTheme(any()) }
        assertTrue(dispatched.isEmpty())
        assertTrue(effects.isEmpty())
    }
}
