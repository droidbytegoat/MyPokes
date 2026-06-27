package com.souza.mypokes.presentation.pokemon

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import com.souza.mypokes.R
import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.presentation.theme.Dimens
import androidx.lifecycle.compose.collectAsStateWithLifecycle

private const val GRID_COLUMNS = 2
private const val LOAD_MORE_THRESHOLD = 4

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun PokemonListScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onNavigateToDetail: (Int) -> Unit,
    viewModel: PokemonListViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.effects.collect { effect ->
            when (effect) {
                is PokemonListEffect.NavigateToDetail -> onNavigateToDetail(effect.pokemonId)
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        Text(
            text = stringResource(R.string.home_title),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(
                start = Dimens.paddingL,
                end = Dimens.paddingL,
                top = Dimens.paddingL,
                bottom = Dimens.paddingXs,
            ),
        )

        SearchBarWithAutocomplete(
            query = state.searchQuery,
            suggestions = state.autocompleteItems,
            onQueryChange = { viewModel.dispatch(PokemonListIntent.Search(it)) },
            onSuggestionClick = { viewModel.dispatch(PokemonListIntent.Search(it)) },
            onClear = { viewModel.dispatch(PokemonListIntent.ClearSearch) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Dimens.paddingL, vertical = Dimens.paddingS),
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading && state.pokemon.isEmpty() -> LoadingContent()
                state.error != null && state.pokemon.isEmpty() -> ErrorContent(
                    message = state.error!!,
                    onRetry = { viewModel.dispatch(PokemonListIntent.LoadInitial) },
                )
                state.displayList.isEmpty() && state.isSearchActive -> SearchEmptyContent(state.searchQuery)
                state.displayList.isEmpty() -> EmptyContent()
                else -> PokemonGridContent(
                    state = state,
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    onPokemonClick = { viewModel.dispatch(PokemonListIntent.OnPokemonClick(it)) },
                    onFavoriteClick = { pokemon -> viewModel.dispatch(PokemonListIntent.ToggleFavorite(pokemon)) },
                    onLoadMore = { viewModel.dispatch(PokemonListIntent.LoadNextPage) },
                    onRefresh = { viewModel.dispatch(PokemonListIntent.Refresh) },
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SearchBarWithAutocomplete(
    query: String,
    suggestions: List<String>,
    onQueryChange: (String) -> Unit,
    onSuggestionClick: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(suggestions) {
        expanded = suggestions.isNotEmpty()
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier,
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            placeholder = { Text(stringResource(R.string.search_placeholder)) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search_cd)) },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = {
                        onClear()
                        expanded = false
                    }) {
                        Icon(Icons.Default.Close, contentDescription = stringResource(R.string.search_clear_cd))
                    }
                }
            },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(MenuAnchorType.PrimaryEditable),
        )

        if (suggestions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                suggestions.forEach { name ->
                    DropdownMenuItem(
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                        text = {
                            Text(
                                text = name,
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        },
                        onClick = {
                            onSuggestionClick(name)
                            expanded = false
                        },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun PokemonGridContent(
    state: PokemonListState,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    onPokemonClick: (Int) -> Unit,
    onFavoriteClick: (Pokemon) -> Unit,
    onLoadMore: () -> Unit,
    onRefresh: () -> Unit,
) {
    val gridState = rememberLazyGridState()
    val shouldLoadMore by remember {
        derivedStateOf {
            val lastVisible = gridState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0
            val total = gridState.layoutInfo.totalItemsCount
            total > 0 && lastVisible >= total - LOAD_MORE_THRESHOLD
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !state.isSearchActive) onLoadMore()
    }

    PullToRefreshBox(
        isRefreshing = state.isRefreshing,
        onRefresh = onRefresh,
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(GRID_COLUMNS),
            state = gridState,
            contentPadding = PaddingValues(Dimens.cardPadding),
            verticalArrangement = Arrangement.spacedBy(Dimens.paddingS),
            horizontalArrangement = Arrangement.spacedBy(Dimens.paddingS),
        ) {
            items(
                items = state.displayList,
                key = { it.id },
            ) { pokemon ->
                PokemonCard(
                    pokemon = pokemon,
                    isFavorite = pokemon.id in state.favoriteIds,
                    onClick = { onPokemonClick(pokemon.id) },
                    onFavoriteClick = { onFavoriteClick(pokemon) },
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                )
            }

            if (state.isLoadingMore) {
                item(span = { GridItemSpan(GRID_COLUMNS) }) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Dimens.paddingL),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(Dimens.paddingHuge))
                    }
                }
            }
        }
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
            .padding(Dimens.paddingHuge),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.load_error_title),
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

@Composable
private fun EmptyContent() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.no_pokemon),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SearchEmptyContent(query: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(
            text = stringResource(R.string.no_results, query),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
    }
}
