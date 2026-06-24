package com.souza.mypokes.presentation.theme

import androidx.compose.ui.graphics.Color

val PokemonRed = Color(0xFFCC0000)
val PokemonRedDark = Color(0xFF8B0000)
val PokemonRedContainer = Color(0xFFFFDAD6)
val OnPokemonRedContainer = Color(0xFF410002)

val DarkSurface = Color(0xFF1C1B1F)
val DarkBackground = Color(0xFF1C1B1F)

// Pokémon type palette
val TypeFire = Color(0xFFF08030)
val TypeWater = Color(0xFF6890F0)
val TypeGrass = Color(0xFF78C850)
val TypeElectric = Color(0xFFF8D030)
val TypePsychic = Color(0xFFF85888)
val TypeIce = Color(0xFF98D8D8)
val TypeDragon = Color(0xFF7038F8)
val TypeDark = Color(0xFF705848)
val TypeFairy = Color(0xFFEE99AC)
val TypeFighting = Color(0xFFC03028)
val TypeFlying = Color(0xFFA890F0)
val TypePoison = Color(0xFFA040A0)
val TypeGround = Color(0xFFE0C068)
val TypeRock = Color(0xFFB8A038)
val TypeBug = Color(0xFFA8B820)
val TypeGhost = Color(0xFF705898)
val TypeSteel = Color(0xFFB8B8D0)
val TypeNormal = Color(0xFFA8A878)
val TypeUnknown = Color(0xFF68A090)

fun pokemonTypeColor(type: String): Color = when (type.lowercase()) {
    "fire" -> TypeFire
    "water" -> TypeWater
    "grass" -> TypeGrass
    "electric" -> TypeElectric
    "psychic" -> TypePsychic
    "ice" -> TypeIce
    "dragon" -> TypeDragon
    "dark" -> TypeDark
    "fairy" -> TypeFairy
    "fighting" -> TypeFighting
    "flying" -> TypeFlying
    "poison" -> TypePoison
    "ground" -> TypeGround
    "rock" -> TypeRock
    "bug" -> TypeBug
    "ghost" -> TypeGhost
    "steel" -> TypeSteel
    "normal" -> TypeNormal
    else -> TypeUnknown
}
