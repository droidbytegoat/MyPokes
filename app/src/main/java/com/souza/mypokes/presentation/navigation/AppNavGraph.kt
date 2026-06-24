package com.souza.mypokes.presentation.navigation

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
import com.souza.mypokes.presentation.splash.SplashScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier,
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToMain = {
                    navController.navigate(Screen.PokemonList.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                },
            )
        }

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
        ) {
            PokemonDetailScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
