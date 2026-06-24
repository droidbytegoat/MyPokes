package com.souza.mypokes.presentation.detail

import com.souza.mypokes.domain.model.PokemonDetail
import com.souza.mypokes.domain.model.PokemonStat
import com.souza.mypokes.domain.model.PokemonType
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PokemonDetailReducerTest {

    private val reducer = PokemonDetailReducer()
    private val initial = PokemonDetailState()

    private val fakePokemon = PokemonDetail(
        id = 1,
        name = "bulbasaur",
        imageUrl = "https://example.com/1.png",
        types = listOf(PokemonType("grass", 1), PokemonType("poison", 2)),
        height = 7,
        weight = 69,
        stats = listOf(PokemonStat("hp", 45), PokemonStat("attack", 49)),
    )

    // ── LoadDetail ───────────────────────────────────────────────────────────

    @Test
    fun `LoadDetail sets isLoading and clears error`() {
        val withError = initial.copy(error = "previous error")
        val result = reducer.reduce(withError, PokemonDetailIntent.LoadDetail(1))
        assertTrue(result.isLoading)
        assertNull(result.error)
    }

    // ── DetailLoaded ─────────────────────────────────────────────────────────

    @Test
    fun `DetailLoaded stores pokemon and clears loading`() {
        val loading = reducer.reduce(initial, PokemonDetailIntent.LoadDetail(1))
        val result = reducer.reduce(loading, PokemonDetailIntent.DetailLoaded(fakePokemon))
        assertEquals(fakePokemon, result.pokemon)
        assertFalse(result.isLoading)
        assertNull(result.error)
    }

    // ── FavoriteStatusLoaded ─────────────────────────────────────────────────

    @Test
    fun `FavoriteStatusLoaded sets isFavorite to true`() {
        val result = reducer.reduce(initial, PokemonDetailIntent.FavoriteStatusLoaded(true))
        assertTrue(result.isFavorite)
    }

    @Test
    fun `FavoriteStatusLoaded sets isFavorite to false`() {
        val withFav = initial.copy(isFavorite = true)
        val result = reducer.reduce(withFav, PokemonDetailIntent.FavoriteStatusLoaded(false))
        assertFalse(result.isFavorite)
    }

    // ── ToggleFavorite ───────────────────────────────────────────────────────

    @Test
    fun `ToggleFavorite flips isFavorite from false to true`() {
        val result = reducer.reduce(initial.copy(isFavorite = false), PokemonDetailIntent.ToggleFavorite)
        assertTrue(result.isFavorite)
    }

    @Test
    fun `ToggleFavorite flips isFavorite from true to false`() {
        val result = reducer.reduce(initial.copy(isFavorite = true), PokemonDetailIntent.ToggleFavorite)
        assertFalse(result.isFavorite)
    }

    // ── LoadFailed ───────────────────────────────────────────────────────────

    @Test
    fun `LoadFailed clears loading and sets error message`() {
        val loading = reducer.reduce(initial, PokemonDetailIntent.LoadDetail(1))
        val result = reducer.reduce(loading, PokemonDetailIntent.LoadFailed("Network error"))
        assertFalse(result.isLoading)
        assertEquals("Network error", result.error)
    }

    // ── NavigateBack ─────────────────────────────────────────────────────────

    @Test
    fun `NavigateBack does not change state`() {
        val result = reducer.reduce(initial, PokemonDetailIntent.NavigateBack)
        assertEquals(initial, result)
    }
}
