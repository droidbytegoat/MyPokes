package com.souza.mypokes.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.souza.mypokes.presentation.detail.PokemonDetailScreen
import com.souza.mypokes.presentation.favorites.FavoritesScreen
import com.souza.mypokes.presentation.settings.SettingsScreen
import com.souza.mypokes.presentation.pokemon.PokemonListScreen

private const val ANIM_DURATION = 300

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.PokemonList.route,
        modifier = modifier,
    ) {
        composable(Screen.PokemonList.route) {
            PokemonListScreen(
                onNavigateToDetail = { pokemonId ->
                    navController.navigate(Screen.PokemonDetail.createRoute(pokemonId))
                },
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                onNavigateToDetail = { pokemonId ->
                    navController.navigate(Screen.PokemonDetail.createRoute(pokemonId))
                },
            )
        }

        composable(Screen.Settings.route) {
            SettingsScreen()
        }

        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(
                navArgument(Screen.PokemonDetail.Args.POKEMON_ID) { type = NavType.IntType }
            ),
            enterTransition = {
                scaleIn(initialScale = 0.85f, animationSpec = tween(ANIM_DURATION)) +
                    fadeIn(animationSpec = tween(ANIM_DURATION))
            },
            exitTransition = {
                scaleOut(targetScale = 0.85f, animationSpec = tween(ANIM_DURATION)) +
                    fadeOut(animationSpec = tween(ANIM_DURATION))
            },
            popEnterTransition = {
                fadeIn(animationSpec = tween(ANIM_DURATION))
            },
            popExitTransition = {
                scaleOut(targetScale = 0.85f, animationSpec = tween(ANIM_DURATION)) +
                    fadeOut(animationSpec = tween(ANIM_DURATION))
            },
        ) {
            PokemonDetailScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
