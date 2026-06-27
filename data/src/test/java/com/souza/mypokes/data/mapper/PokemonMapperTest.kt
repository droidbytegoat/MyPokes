package com.souza.mypokes.data.mapper

import com.souza.mypokes.data.remote.dto.PokemonDetailDto
import com.souza.mypokes.data.remote.dto.PokemonEntryDto
import com.souza.mypokes.data.remote.dto.PokemonOfficialArtworkDto
import com.souza.mypokes.data.remote.dto.PokemonOtherSpritesDto
import com.souza.mypokes.data.remote.dto.PokemonSpritesDto
import com.souza.mypokes.data.remote.dto.PokemonStatDto
import com.souza.mypokes.data.remote.dto.PokemonStatSlotDto
import com.souza.mypokes.data.remote.dto.PokemonTypeDto
import com.souza.mypokes.data.remote.dto.PokemonTypeSlotDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PokemonMapperTest {

    private val detailDto = PokemonDetailDto(
        id = 1,
        name = "bulbasaur",
        height = 7,
        weight = 69,
        types = listOf(PokemonTypeSlotDto(slot = 1, type = PokemonTypeDto("grass", "url"))),
        stats = listOf(PokemonStatSlotDto(baseStat = 45, stat = PokemonStatDto("hp", "url"))),
        sprites = PokemonSpritesDto(
            other = PokemonOtherSpritesDto(
                officialArtwork = PokemonOfficialArtworkDto("https://example.com/1.png")
            )
        ),
    )

    @Test
    fun `mapToPokemonFromEntry extracts id from url`() {
        val entry = PokemonEntryDto("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/")
        val pokemon = PokemonMapper.mapToPokemonFromEntry(entry)
        assertEquals(1, pokemon.id)
    }

    @Test
    fun `mapToPokemonFromEntry constructs official artwork url`() {
        val entry = PokemonEntryDto("bulbasaur", "https://pokeapi.co/api/v2/pokemon/1/")
        val pokemon = PokemonMapper.mapToPokemonFromEntry(entry)
        assertTrue(pokemon.imageUrl.contains("official-artwork/1.png"))
    }

    @Test
    fun `mapToPokemonFromEntry capitalizes hyphenated name`() {
        val entry = PokemonEntryDto("mr-mime", "https://pokeapi.co/api/v2/pokemon/122/")
        val pokemon = PokemonMapper.mapToPokemonFromEntry(entry)
        assertEquals("Mr Mime", pokemon.name)
    }

    @Test
    fun `mapToPokemon uses official artwork url from dto`() {
        val pokemon = PokemonMapper.mapToPokemon(detailDto)
        assertEquals("https://example.com/1.png", pokemon.imageUrl)
    }

    @Test
    fun `mapToPokemon falls back to constructed url when artwork is null`() {
        val dto = detailDto.copy(
            sprites = PokemonSpritesDto(
                other = PokemonOtherSpritesDto(officialArtwork = PokemonOfficialArtworkDto(null))
            )
        )
        val pokemon = PokemonMapper.mapToPokemon(dto)
        assertTrue(pokemon.imageUrl.contains("official-artwork/1.png"))
    }

    @Test
    fun `mapToDetail maps id, name, height, weight correctly`() {
        val detail = PokemonMapper.mapToDetail(detailDto)
        assertEquals(1, detail.id)
        assertEquals("Bulbasaur", detail.name)
        assertEquals(7, detail.height)
        assertEquals(69, detail.weight)
    }

    @Test
    fun `mapToDetail maps types sorted by slot`() {
        val dto = detailDto.copy(
            types = listOf(
                PokemonTypeSlotDto(slot = 2, type = PokemonTypeDto("poison", "url")),
                PokemonTypeSlotDto(slot = 1, type = PokemonTypeDto("grass", "url")),
            )
        )
        val detail = PokemonMapper.mapToDetail(dto)
        assertEquals("grass", detail.types[0].name)
        assertEquals(1, detail.types[0].slot)
        assertEquals("poison", detail.types[1].name)
    }

    @Test
    fun `mapToDetail maps stats correctly`() {
        val detail = PokemonMapper.mapToDetail(detailDto)
        assertEquals(1, detail.stats.size)
        assertEquals("hp", detail.stats.first().name)
        assertEquals(45, detail.stats.first().baseStat)
    }

    @Test
    fun `mapToDetail capitalizes name with hyphen`() {
        val dto = detailDto.copy(name = "farfetch-d")
        val detail = PokemonMapper.mapToDetail(dto)
        assertEquals("Farfetch D", detail.name)
    }
}
