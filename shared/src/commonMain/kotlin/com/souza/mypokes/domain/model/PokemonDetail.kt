package com.souza.mypokes.domain.model

data class PokemonDetail(
    val id: Int,
    val name: String,
    val imageUrl: String,
    val types: List<PokemonType>,
    val height: Int,
    val weight: Int,
    val stats: List<PokemonStat>,
)
