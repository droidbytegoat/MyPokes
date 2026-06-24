package com.souza.mypokes.presentation.settings

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.souza.mypokes.R
import com.souza.mypokes.data.preferences.AppTheme
import com.souza.mypokes.presentation.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.screen_settings),
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                windowInsets = WindowInsets(0),
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else {
                SettingsContent(
                    selectedTheme = state.selectedTheme,
                    onThemeSelected = { viewModel.dispatch(SettingsIntent.SetTheme(it)) },
                )
            }
        }
    }
}

@Composable
private fun SettingsContent(
    selectedTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
) {
    Column(modifier = Modifier.fillMaxSize()) {
        SectionHeader(title = stringResource(R.string.section_appearance))

        ThemeOption.entries.forEach { option ->
            ThemeRow(
                option = option,
                selected = selectedTheme == option.theme,
                onSelect = { onThemeSelected(option.theme) },
            )
            HorizontalDivider(modifier = Modifier.padding(start = Dimens.statDividerStart))
        }
    }
}

@Composable
private fun SectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelMedium,
        color = MaterialTheme.colorScheme.primary,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(horizontal = Dimens.paddingL, vertical = Dimens.paddingM),
    )
}

@Composable
private fun ThemeRow(
    option: ThemeOption,
    selected: Boolean,
    onSelect: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(role = Role.RadioButton, onClick = onSelect)
            .padding(horizontal = Dimens.paddingL, vertical = Dimens.paddingXs),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        RadioButton(selected = selected, onClick = onSelect)
        Column(modifier = Modifier.padding(start = Dimens.paddingL)) {
            Text(
                text = stringResource(option.labelRes),
                style = MaterialTheme.typography.bodyLarge,
            )
            Spacer(modifier = Modifier.height(Dimens.paddingXs / 2))
            Text(
                text = stringResource(option.descRes),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

private enum class ThemeOption(
    val theme: AppTheme,
    @StringRes val labelRes: Int,
    @StringRes val descRes: Int,
) {
    LIGHT(AppTheme.LIGHT, R.string.theme_light, R.string.theme_light_desc),
    DARK(AppTheme.DARK, R.string.theme_dark, R.string.theme_dark_desc),
    SYSTEM(AppTheme.FOLLOW_SYSTEM, R.string.theme_system, R.string.theme_system_desc),
}
