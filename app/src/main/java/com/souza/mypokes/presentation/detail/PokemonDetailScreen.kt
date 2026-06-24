package com.souza.mypokes.presentation.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.souza.mypokes.domain.model.PokemonDetail
import com.souza.mypokes.domain.model.PokemonStat

private const val MAX_STAT_VALUE = 255f

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PokemonDetailScreen(
    onNavigateBack: () -> Unit,
    viewModel: PokemonDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

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
                            contentDescription = "Back",
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
                            contentDescription = if (state.isFavorite) "Remove from favorites" else "Add to favorites",
                            tint = if (state.isFavorite) Color.Red else MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                state.isLoading -> LoadingContent()
                state.error != null -> ErrorContent(
                    message = state.error!!,
                    onRetry = { viewModel.dispatch(PokemonDetailIntent.LoadDetail(0)) },
                )
                state.pokemon != null -> DetailContent(pokemon = state.pokemon!!)
            }
        }
    }
}

@Composable
private fun DetailContent(pokemon: PokemonDetail) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
    ) {
        AsyncImage(
            model = pokemon.imageUrl,
            contentDescription = pokemon.name,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .padding(24.dp),
        )

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            TypesRow(types = pokemon.types.map { it.name })

            Spacer(modifier = Modifier.height(20.dp))

            PhysicalStats(height = pokemon.height, weight = pokemon.weight)

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Base Stats",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(12.dp))

            pokemon.stats.forEach { stat ->
                StatRow(stat = stat)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TypesRow(types: List<String>) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        types.forEach { type ->
            TypeChip(type = type)
        }
    }
}

@Composable
private fun TypeChip(type: String) {
    Surface(
        color = typeColor(type),
        shape = RoundedCornerShape(50),
    ) {
        Text(
            text = type.replaceFirstChar { it.uppercase() },
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
        )
    }
}

@Composable
private fun PhysicalStats(height: Int, weight: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        PhysicalStatItem(label = "Height", value = "${"%.1f".format(height / 10f)} m")
        PhysicalStatItem(label = "Weight", value = "${"%.1f".format(weight / 10f)} kg")
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
            modifier = Modifier.width(72.dp),
        )
        Text(
            text = stat.baseStat.toString().padStart(3),
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.End,
            modifier = Modifier.width(32.dp),
        )
        Spacer(modifier = Modifier.width(8.dp))
        LinearProgressIndicator(
            progress = { (stat.baseStat / MAX_STAT_VALUE).coerceIn(0f, 1f) },
            modifier = Modifier
                .weight(1f)
                .height(8.dp),
        )
    }
}

@Composable
private fun LoadingContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Could not load Pokémon.",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(onClick = onRetry) { Text("Try again") }
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

private fun typeColor(type: String): Color = when (type.lowercase()) {
    "fire" -> Color(0xFFF08030)
    "water" -> Color(0xFF6890F0)
    "grass" -> Color(0xFF78C850)
    "electric" -> Color(0xFFF8D030)
    "psychic" -> Color(0xFFF85888)
    "ice" -> Color(0xFF98D8D8)
    "dragon" -> Color(0xFF7038F8)
    "dark" -> Color(0xFF705848)
    "fairy" -> Color(0xFFEE99AC)
    "fighting" -> Color(0xFFC03028)
    "flying" -> Color(0xFFA890F0)
    "poison" -> Color(0xFFA040A0)
    "ground" -> Color(0xFFE0C068)
    "rock" -> Color(0xFFB8A038)
    "bug" -> Color(0xFFA8B820)
    "ghost" -> Color(0xFF705898)
    "steel" -> Color(0xFFB8B8D0)
    "normal" -> Color(0xFFA8A878)
    else -> Color(0xFF68A090)
}
