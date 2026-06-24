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
        ) {
            PokemonDetailScreen(
                onNavigateBack = { navController.popBackStack() },
            )
        }
    }
}
