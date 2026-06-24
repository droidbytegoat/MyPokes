package com.souza.mypokes.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.souza.mypokes.data.preferences.AppTheme

private val LightColorScheme = lightColorScheme(
    primary = PokemonRed,
    onPrimary = androidx.compose.ui.graphics.Color.White,
    primaryContainer = PokemonRedContainer,
    onPrimaryContainer = OnPokemonRedContainer,
)

private val DarkColorScheme = darkColorScheme(
    primary = PokemonRedContainer,
    onPrimary = OnPokemonRedContainer,
    primaryContainer = PokemonRedDark,
    onPrimaryContainer = PokemonRedContainer,
)

@Composable
fun MyPokesTheme(
    appTheme: AppTheme = AppTheme.FOLLOW_SYSTEM,
    content: @Composable () -> Unit,
) {
    val darkTheme = when (appTheme) {
        AppTheme.LIGHT -> false
        AppTheme.DARK -> true
        AppTheme.FOLLOW_SYSTEM -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content,
    )
}
