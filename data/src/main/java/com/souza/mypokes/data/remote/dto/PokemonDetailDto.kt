package com.souza.mypokes.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PokemonDetailDto(
    val id: Int,
    val name: String,
    val height: Int,
    val weight: Int,
    val types: List<PokemonTypeSlotDto>,
    val stats: List<PokemonStatSlotDto>,
    val sprites: PokemonSpritesDto,
)

@Serializable
data class PokemonTypeSlotDto(
    val slot: Int,
    val type: PokemonTypeDto,
)

@Serializable
data class PokemonTypeDto(
    val name: String,
    val url: String,
)

@Serializable
data class PokemonStatSlotDto(
    @SerialName("base_stat") val baseStat: Int,
    val stat: PokemonStatDto,
)

@Serializable
data class PokemonStatDto(
    val name: String,
    val url: String,
)

@Serializable
data class PokemonSpritesDto(
    val other: PokemonOtherSpritesDto,
)

@Serializable
data class PokemonOtherSpritesDto(
    @SerialName("official-artwork") val officialArtwork: PokemonOfficialArtworkDto,
)

@Serializable
data class PokemonOfficialArtworkDto(
    @SerialName("front_default") val frontDefault: String?,
)
