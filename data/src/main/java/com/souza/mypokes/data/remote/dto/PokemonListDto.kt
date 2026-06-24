package com.souza.mypokes.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PokemonListResponseDto(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<PokemonEntryDto>,
)

@Serializable
data class PokemonEntryDto(
    val name: String,
    val url: String,
)
