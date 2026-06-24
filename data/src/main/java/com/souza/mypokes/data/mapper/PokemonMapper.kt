package com.souza.mypokes.data.mapper

import com.souza.mypokes.data.remote.dto.PokemonDetailDto
import com.souza.mypokes.data.remote.dto.PokemonEntryDto
import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.model.PokemonDetail
import com.souza.mypokes.domain.model.PokemonStat
import com.souza.mypokes.domain.model.PokemonType

object PokemonMapper {

    fun mapToPokemonFromEntry(dto: PokemonEntryDto): Pokemon {
        val id = extractIdFromUrl(dto.url)
        return Pokemon(
            id = id,
            name = dto.name.toDisplayName(),
            imageUrl = officialArtworkUrl(id),
        )
    }

    fun mapToPokemon(dto: PokemonDetailDto): Pokemon = Pokemon(
        id = dto.id,
        name = dto.name.toDisplayName(),
        imageUrl = dto.sprites.other.officialArtwork.frontDefault ?: officialArtworkUrl(dto.id),
    )

    fun mapToDetail(dto: PokemonDetailDto): PokemonDetail = PokemonDetail(
        id = dto.id,
        name = dto.name.toDisplayName(),
        imageUrl = dto.sprites.other.officialArtwork.frontDefault ?: officialArtworkUrl(dto.id),
        types = dto.types
            .sortedBy { it.slot }
            .map { PokemonType(name = it.type.name, slot = it.slot) },
        height = dto.height,
        weight = dto.weight,
        stats = dto.stats.map { PokemonStat(name = it.stat.name, baseStat = it.baseStat) },
    )

    private fun extractIdFromUrl(url: String): Int =
        url.trimEnd('/').substringAfterLast('/').toInt()

    private fun officialArtworkUrl(id: Int): String =
        "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/$id.png"

    private fun String.toDisplayName(): String =
        split("-").joinToString(" ") { part -> part.replaceFirstChar { it.uppercase() } }
}
