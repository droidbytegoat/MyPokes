package com.souza.mypokes.presentation.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
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
            // Implemented in Step 9
            PlaceholderScreen("Favorites")
        }

        composable(Screen.Settings.route) {
            // Implemented in Step 10
            PlaceholderScreen("Settings")
        }

        composable(
            route = Screen.PokemonDetail.route,
            arguments = listOf(
                navArgument(Screen.PokemonDetail.Args.POKEMON_ID) { type = NavType.IntType }
            ),
        ) {
            // Implemented in Step 8
            PlaceholderScreen("Pokémon Detail")
        }
    }
}

@Composable
private fun PlaceholderScreen(name: String) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = name)
    }
}
