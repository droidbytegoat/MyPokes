package com.souza.mypokes.presentation.navigation

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.souza.mypokes.data.preferences.AppTheme
import com.souza.mypokes.presentation.detail.PokemonDetailScreen
import com.souza.mypokes.presentation.favorites.FavoritesScreen
import com.souza.mypokes.presentation.settings.SettingsScreen
import com.souza.mypokes.presentation.pokemon.PokemonListScreen

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun AppNavGraph(
    navController: NavHostController,
    appTheme: AppTheme,
    modifier: Modifier = Modifier,
) {
    SharedTransitionLayout {
        NavHost(
            navController = navController,
            startDestination = Screen.PokemonList.route,
            modifier = modifier,
        ) {
            composable(Screen.PokemonList.route) {
                PokemonListScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    onNavigateToDetail = { pokemonId ->
                        navController.navigate(Screen.PokemonDetail.createRoute(pokemonId))
                    },
                )
            }

            composable(Screen.Favorites.route) {
                FavoritesScreen(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    onNavigateToDetail = { pokemonId ->
                        navController.navigate(Screen.PokemonDetail.createRoute(pokemonId))
                    },
                )
            }

            composable(Screen.Settings.route) {
                key(appTheme) {
                    SettingsScreen()
                }
            }

            composable(
                route = Screen.PokemonDetail.route,
                arguments = listOf(
                    navArgument(Screen.PokemonDetail.Args.POKEMON_ID) { type = NavType.IntType }
                ),
            ) { backStackEntry ->
                val pokemonId = backStackEntry.arguments
                    ?.getInt(Screen.PokemonDetail.Args.POKEMON_ID) ?: 0
                PokemonDetailScreen(
                    pokemonId = pokemonId,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this,
                    onNavigateBack = { navController.popBackStack() },
                )
            }
        }
    }
}
