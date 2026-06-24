package com.souza.mypokes.presentation.pokemon

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.usecase.GetPokemonListUseCase
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class PokemonListReducerTest {

    private val reducer = PokemonListReducer()
    private val initial = PokemonListState()

    private fun pokemon(id: Int, name: String = "pokemon-$id") =
        Pokemon(id = id, name = name, imageUrl = "url-$id")

    private val page = (1..GetPokemonListUseCase.PAGE_SIZE).map { pokemon(it) }

    // ── LoadInitial ──────────────────────────────────────────────────────────

    @Test
    fun `LoadInitial sets isLoading and isRequestInFlight`() {
        val result = reducer.reduce(initial, PokemonListIntent.LoadInitial)
        assertTrue(result.isLoading)
        assertTrue(result.isRequestInFlight)
        assertNull(result.error)
    }

    // ── Refresh ──────────────────────────────────────────────────────────────

    @Test
    fun `Refresh sets isRefreshing and isRequestInFlight`() {
        val result = reducer.reduce(initial, PokemonListIntent.Refresh)
        assertTrue(result.isRefreshing)
        assertTrue(result.isRequestInFlight)
        assertFalse(result.isLoading)
    }

    // ── LoadNextPage ─────────────────────────────────────────────────────────

    @Test
    fun `LoadNextPage sets isLoadingMore when not in flight and has next page`() {
        val state = initial.copy(isRequestInFlight = false, hasNextPage = true)
        val result = reducer.reduce(state, PokemonListIntent.LoadNextPage)
        assertTrue(result.isLoadingMore)
        assertTrue(result.isRequestInFlight)
    }

    @Test
    fun `LoadNextPage is no-op when already in flight`() {
        val state = initial.copy(isRequestInFlight = true, hasNextPage = true)
        assertEquals(state, reducer.reduce(state, PokemonListIntent.LoadNextPage))
    }

    @Test
    fun `LoadNextPage is no-op when no next page`() {
        val state = initial.copy(isRequestInFlight = false, hasNextPage = false)
        assertEquals(state, reducer.reduce(state, PokemonListIntent.LoadNextPage))
    }

    // ── Search / ClearSearch ─────────────────────────────────────────────────

    @Test
    fun `Search sets searchQuery and activates search mode`() {
        val result = reducer.reduce(initial, PokemonListIntent.Search("bulba"))
        assertEquals("bulba", result.searchQuery)
        assertTrue(result.isSearchActive)
    }

    @Test
    fun `Search with blank query does not activate search mode`() {
        val result = reducer.reduce(initial, PokemonListIntent.Search("  "))
        assertFalse(result.isSearchActive)
    }

    @Test
    fun `ClearSearch resets searchQuery`() {
        val withSearch = reducer.reduce(initial, PokemonListIntent.Search("pika"))
        val result = reducer.reduce(withSearch, PokemonListIntent.ClearSearch)
        assertEquals("", result.searchQuery)
        assertFalse(result.isSearchActive)
    }

    // ── PokemonListLoaded ────────────────────────────────────────────────────

    @Test
    fun `PokemonListLoaded replaces list and clears loading flags`() {
        val loading = reducer.reduce(initial, PokemonListIntent.LoadInitial)
        val result = reducer.reduce(loading, PokemonListIntent.PokemonListLoaded(page))
        assertEquals(page, result.pokemon)
        assertFalse(result.isLoading)
        assertFalse(result.isRefreshing)
        assertFalse(result.isRequestInFlight)
        assertNull(result.error)
        assertEquals(GetPokemonListUseCase.PAGE_SIZE, result.currentOffset)
    }

    @Test
    fun `PokemonListLoaded sets hasNextPage false when fewer than PAGE_SIZE returned`() {
        val result = reducer.reduce(initial, PokemonListIntent.PokemonListLoaded(listOf(pokemon(1))))
        assertFalse(result.hasNextPage)
    }

    @Test
    fun `PokemonListLoaded sets hasNextPage true when full page returned`() {
        assertTrue(reducer.reduce(initial, PokemonListIntent.PokemonListLoaded(page)).hasNextPage)
    }

    // ── LoadMoreLoaded ───────────────────────────────────────────────────────

    @Test
    fun `LoadMoreLoaded appends to existing list`() {
        val existingState = initial.copy(
            pokemon = listOf(pokemon(1)),
            currentOffset = 1,
            isLoadingMore = true,
            isRequestInFlight = true,
        )
        val result = reducer.reduce(existingState, PokemonListIntent.LoadMoreLoaded(listOf(pokemon(2), pokemon(3))))
        assertEquals(3, result.pokemon.size)
        assertFalse(result.isLoadingMore)
        assertFalse(result.isRequestInFlight)
        assertEquals(3, result.currentOffset)
    }

    // ── UpdateFavorites ──────────────────────────────────────────────────────

    @Test
    fun `UpdateFavorites replaces favoriteIds set`() {
        val ids = setOf(1, 4, 7)
        assertEquals(ids, reducer.reduce(initial, PokemonListIntent.UpdateFavorites(ids)).favoriteIds)
    }

    // ── LoadFailed ───────────────────────────────────────────────────────────

    @Test
    fun `LoadFailed clears all loading flags and sets error`() {
        val loading = reducer.reduce(initial, PokemonListIntent.LoadInitial)
        val result = reducer.reduce(loading, PokemonListIntent.LoadFailed("Network error"))
        assertFalse(result.isLoading)
        assertFalse(result.isLoadingMore)
        assertFalse(result.isRefreshing)
        assertFalse(result.isRequestInFlight)
        assertEquals("Network error", result.error)
    }

    // ── displayList (computed local filter) ──────────────────────────────────

    @Test
    fun `displayList returns all pokemon when query is blank`() {
        val state = initial.copy(pokemon = listOf(pokemon(1, "bulbasaur"), pokemon(2, "ivysaur")))
        assertEquals(state.pokemon, state.displayList)
    }

    @Test
    fun `displayList filters by name containing query`() {
        val state = initial.copy(
            pokemon = listOf(pokemon(1, "bulbasaur"), pokemon(25, "pikachu"), pokemon(2, "ivysaur")),
            searchQuery = "saur",
        )
        assertEquals(listOf(pokemon(1, "bulbasaur"), pokemon(2, "ivysaur")), state.displayList)
    }

    @Test
    fun `displayList filter is case-insensitive`() {
        val state = initial.copy(
            pokemon = listOf(pokemon(1, "bulbasaur")),
            searchQuery = "BULBA",
        )
        assertEquals(1, state.displayList.size)
    }

    // ── autocompleteItems ────────────────────────────────────────────────────

    @Test
    fun `autocompleteItems is empty when query is blank`() {
        val state = initial.copy(pokemon = listOf(pokemon(1, "bulbasaur")), searchQuery = "")
        assertTrue(state.autocompleteItems.isEmpty())
    }

    @Test
    fun `autocompleteItems returns at most 3 suggestions`() {
        val state = initial.copy(
            pokemon = (1..10).map { pokemon(it, "poke-$it") },
            searchQuery = "poke",
        )
        assertTrue(state.autocompleteItems.size <= 3)
    }

    @Test
    fun `autocompleteItems puts startsWith matches before contains matches`() {
        val state = initial.copy(
            pokemon = listOf(
                pokemon(1, "bulbasaur"),   // contains "saur"
                pokemon(2, "sauron"),      // starts with "saur"
            ),
            searchQuery = "saur",
        )
        assertEquals(listOf("sauron", "bulbasaur"), state.autocompleteItems)
    }

    // ── No-op intents ────────────────────────────────────────────────────────

    @Test
    fun `OnPokemonClick does not change state`() {
        assertEquals(initial, reducer.reduce(initial, PokemonListIntent.OnPokemonClick(1)))
    }

    @Test
    fun `ToggleFavorite does not change state`() {
        assertEquals(initial, reducer.reduce(initial, PokemonListIntent.ToggleFavorite(pokemon(1))))
    }
}
