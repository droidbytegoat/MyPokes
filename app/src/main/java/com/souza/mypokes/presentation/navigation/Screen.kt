package com.souza.mypokes.presentation.navigation

sealed class Screen(val route: String) {
    data object PokemonList : Screen("pokemon_list")
    data object Favorites : Screen("favorites")
    data object Settings : Screen("settings")
    data object PokemonDetail : Screen("pokemon_detail/{${Args.POKEMON_ID}}") {
        object Args {
            const val POKEMON_ID = "pokemonId"
        }
        fun createRoute(pokemonId: Int) = "pokemon_detail/$pokemonId"
    }
}
