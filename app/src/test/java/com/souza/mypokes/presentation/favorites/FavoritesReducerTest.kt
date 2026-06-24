package com.souza.mypokes.presentation.favorites

import com.souza.mypokes.domain.model.Pokemon
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FavoritesReducerTest {

    private val reducer = FavoritesReducer()
    private val initial = FavoritesState()

    private fun pokemon(id: Int) = Pokemon(id = id, name = "pokemon-$id", imageUrl = "url-$id")

    // ── FavoritesLoaded ──────────────────────────────────────────────────────

    @Test
    fun `FavoritesLoaded populates list and clears loading`() {
        val list = listOf(pokemon(1), pokemon(2))
        val result = reducer.reduce(initial, FavoritesIntent.FavoritesLoaded(list))
        assertEquals(list, result.favorites)
        assertFalse(result.isLoading)
    }

    @Test
    fun `FavoritesLoaded with empty list clears loading`() {
        val result = reducer.reduce(initial, FavoritesIntent.FavoritesLoaded(emptyList()))
        assertTrue(result.favorites.isEmpty())
        assertFalse(result.isLoading)
    }

    @Test
    fun `FavoritesLoaded replaces existing list`() {
        val first = reducer.reduce(initial, FavoritesIntent.FavoritesLoaded(listOf(pokemon(1))))
        val updated = listOf(pokemon(2), pokemon(3))
        val result = reducer.reduce(first, FavoritesIntent.FavoritesLoaded(updated))
        assertEquals(updated, result.favorites)
    }

    // ── Initial state ────────────────────────────────────────────────────────

    @Test
    fun `initial state has isLoading true and empty favorites`() {
        assertTrue(initial.isLoading)
        assertTrue(initial.favorites.isEmpty())
    }

    // ── No-op intents ────────────────────────────────────────────────────────

    @Test
    fun `OnPokemonClick does not change state`() {
        val withData = initial.copy(favorites = listOf(pokemon(1)), isLoading = false)
        val result = reducer.reduce(withData, FavoritesIntent.OnPokemonClick(1))
        assertEquals(withData, result)
    }

    @Test
    fun `RemoveFavorite does not change state`() {
        val poke = pokemon(25)
        val withData = initial.copy(favorites = listOf(poke), isLoading = false)
        val result = reducer.reduce(withData, FavoritesIntent.RemoveFavorite(poke))
        assertEquals(withData, result)
    }
}
