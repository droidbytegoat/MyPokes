package com.souza.mypokes.presentation.pokemon

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.OverlayClip
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.palette.graphics.Palette
import androidx.compose.ui.platform.LocalContext
import coil3.BitmapImage
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.souza.mypokes.R
import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.presentation.theme.Dimens
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PokemonCard(
    pokemon: Pokemon,
    isFavorite: Boolean,
    onClick: () -> Unit,
    onFavoriteClick: () -> Unit,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    modifier: Modifier = Modifier,
) {
    var dominantColor by remember(pokemon.id) { mutableStateOf<Color?>(null) }
    val scope = rememberCoroutineScope()
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    val containerColor = dominantColor
        ?.let { lerp(surfaceVariant, it, 0.3f) }
        ?: surfaceVariant

    Card(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(0.8f),
        colors = CardDefaults.cardColors(containerColor = containerColor),
    ) {
        Box {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                with(sharedTransitionScope) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(pokemon.imageUrl)
                            .crossfade(true)
                            .build(),
                        contentDescription = pokemon.name,
                        contentScale = ContentScale.Fit,
                        onSuccess = { state ->
                            scope.launch(Dispatchers.Default) {
                                val hardware = (state.result.image as? BitmapImage)?.bitmap ?: return@launch
                                val bitmap = hardware.copy(android.graphics.Bitmap.Config.ARGB_8888, false)
                                val palette = Palette.from(bitmap).generate()
                                bitmap.recycle()
                                val swatch = palette.lightVibrantSwatch
                                    ?: palette.vibrantSwatch
                                    ?: palette.dominantSwatch
                                swatch?.rgb?.let { rgb -> dominantColor = Color(rgb) }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(Dimens.paddingS)
                            .sharedElement(
                                sharedContentState = rememberSharedContentState(key = "pokemon-image-${pokemon.id}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                            ),
                    )
                }
                Text(
                    text = "#${pokemon.id.toString().padStart(3, '0')}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.paddingS),
                )
                Text(
                    text = pokemon.name,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = Dimens.paddingS)
                        .padding(bottom = Dimens.paddingS),
                )
            }

            IconButton(
                onClick = onFavoriteClick,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(Dimens.favoriteIconSize),
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(
                        if (isFavorite) R.string.cd_remove_favorite else R.string.cd_add_favorite
                    ),
                    tint = if (isFavorite) MaterialTheme.colorScheme.error
                           else MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(Dimens.favoriteIconInner),
                )
            }
        }
    }
}
