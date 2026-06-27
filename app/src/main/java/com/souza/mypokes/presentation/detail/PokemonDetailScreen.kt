package com.souza.mypokes.presentation.detail

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.souza.mypokes.R
import com.souza.mypokes.domain.model.PokemonDetail
import com.souza.mypokes.domain.model.PokemonStat
import com.souza.mypokes.presentation.theme.Dimens
import com.souza.mypokes.presentation.theme.pokemonTypeColor

private const val MAX_STAT_VALUE = 255f
private fun officialArtworkUrl(id: Int) =
    "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun PokemonDetailScreen(
    pokemonId: Int,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateBack: () -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    var showImageDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                PokemonDetailEffect.NavigateBack -> onNavigateBack()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = state.pokemon?.name?.replaceFirstChar { it.uppercase() } ?: "",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { viewModel.dispatch(PokemonDetailIntent.NavigateBack) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.cd_back),
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.dispatch(PokemonDetailIntent.ToggleFavorite) },
                        enabled = state.pokemon != null,
                    ) {
                        Icon(
                            imageVector = if (state.isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = stringResource(
                                if (state.isFavorite) R.string.cd_remove_favorite else R.string.cd_add_favorite
                            ),
                            tint = if (state.isFavorite) MaterialTheme.colorScheme.error
                                   else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
                windowInsets = WindowInsets(0),
            )
        },
        contentWindowInsets = WindowInsets(0),
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState()),
        ) {
            with(sharedTransitionScope) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(state.pokemon?.imageUrl ?: officialArtworkUrl(pokemonId))
                        .crossfade(true)
                        .build(),
                    contentDescription = state.pokemon?.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Dimens.heroImageHeight)
                        .padding(Dimens.heroImagePadding)
                        .clickable { showImageDialog = true }
                        .sharedElement(
                            sharedContentState = rememberSharedContentState(key = "pokemon-image-$pokemonId"),
                            animatedVisibilityScope = animatedVisibilityScope,
                        ),
                )
            }

            when {
                state.isLoading -> LoadingSection()
                state.error != null -> ErrorSection(
                    message = state.error!!,
                    onRetry = { viewModel.dispatch(PokemonDetailIntent.LoadDetail(pokemonId)) },
                )
                state.pokemon != null -> StatsSection(pokemon = state.pokemon!!)
            }
        }
    }

    if (showImageDialog) {
        Dialog(
            onDismissRequest = { showImageDialog = false },
            properties = DialogProperties(usePlatformDefaultWidth = false),
        ) {
            var animateIn by remember { mutableStateOf(false) }
            LaunchedEffect(Unit) { animateIn = true }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.85f))
                    .clickable { showImageDialog = false },
                contentAlignment = Alignment.Center,
            ) {
                AnimatedVisibility(
                    visible = animateIn,
                    enter = scaleIn(
                        initialScale = 0.3f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
                    ) + fadeIn(),
                    exit = scaleOut(targetScale = 0.3f) + fadeOut(),
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(state.pokemon?.imageUrl ?: officialArtworkUrl(pokemonId))
                            .crossfade(true)
                            .build(),
                        contentDescription = state.pokemon?.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.paddingXxl),
                    )
                }
            }
        }
    }
}

@Composable
private fun StatsSection(pokemon: PokemonDetail) {
    Column(modifier = Modifier.padding(horizontal = Dimens.paddingXl)) {
        TypesRow(types = pokemon.types.map { it.name })

        Spacer(modifier = Modifier.height(Dimens.statSectionSpacing))

        PhysicalStats(height = pokemon.height, weight = pokemon.weight)

        Spacer(modifier = Modifier.height(Dimens.paddingXxl))

        Text(
            text = stringResource(R.string.base_stats),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(Dimens.paddingM))

        pokemon.stats.forEach { stat ->
            StatRow(stat = stat)
            Spacer(modifier = Modifier.height(Dimens.paddingS))
        }

        Spacer(modifier = Modifier.height(Dimens.paddingXxl))
    }
}

@Composable
private fun TypesRow(types: List<String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(Dimens.paddingS)) {
        types.forEach { type ->
            TypeChip(type = type)
        }
    }
}

@Composable
private fun TypeChip(type: String) {
    Surface(
        color = pokemonTypeColor(type),
        shape = RoundedCornerShape(50),
    ) {
        Text(
            text = type.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = Dimens.paddingL, vertical = Dimens.paddingXs + Dimens.paddingXs),
        )
    }
}

@Composable
private fun PhysicalStats(height: Int, weight: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        PhysicalStatItem(
            label = stringResource(R.string.stat_height),
            value = "${"%.1f".format(height / 10f)} m",
        )
        PhysicalStatItem(
            label = stringResource(R.string.stat_weight),
            value = "${"%.1f".format(weight / 10f)} kg",
        )
    }
}

@Composable
private fun PhysicalStatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun StatRow(stat: PokemonStat) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = statDisplayName(stat.name),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(Dimens.statLabelWidth),
        )
        Text(
            text = stat.baseStat.toString().padStart(3),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier.width(Dimens.statValueWidth),
        )
        Spacer(modifier = Modifier.width(Dimens.paddingS))
        LinearProgressIndicator(
            progress = { (stat.baseStat / MAX_STAT_VALUE).coerceIn(0f, 1f) },
            modifier = Modifier
                .weight(1f)
                .height(Dimens.statBarHeight),
        )
    }
}

@Composable
private fun LoadingSection() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Dimens.heroImageHeight),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorSection(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Dimens.paddingHuge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.load_error_detail),
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(Dimens.paddingS))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(Dimens.paddingXxl))
        Button(onClick = onRetry) { Text(stringResource(R.string.try_again)) }
    }
}

private fun statDisplayName(name: String): String = when (name) {
    "hp" -> "HP"
    "attack" -> "Atk"
    "defense" -> "Def"
    "special-attack" -> "Sp.Atk"
    "special-defense" -> "Sp.Def"
    "speed" -> "Speed"
    else -> name.replaceFirstChar { it.uppercase() }
}
