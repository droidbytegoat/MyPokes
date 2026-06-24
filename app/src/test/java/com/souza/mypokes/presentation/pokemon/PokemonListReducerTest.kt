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

    private fun pokemon(id: Int) = Pokemon(id = id, name = "pokemon-$id", imageUrl = "url-$id")
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
        val result = reducer.reduce(state, PokemonListIntent.LoadNextPage)
        assertEquals(state, result)
    }

    @Test
    fun `LoadNextPage is no-op when no next page`() {
        val state = initial.copy(isRequestInFlight = false, hasNextPage = false)
        val result = reducer.reduce(state, PokemonListIntent.LoadNextPage)
        assertEquals(state, result)
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
    fun `ClearSearch resets search state`() {
        val withSearch = initial.copy(
            searchQuery = "pika",
            isSearchActive = true,
            searchResults = listOf(pokemon(25)),
        )
        val result = reducer.reduce(withSearch, PokemonListIntent.ClearSearch)
        assertEquals("", result.searchQuery)
        assertFalse(result.isSearchActive)
        assertTrue(result.searchResults.isEmpty())
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
        val smallPage = listOf(pokemon(1), pokemon(2))
        val result = reducer.reduce(initial, PokemonListIntent.PokemonListLoaded(smallPage))
        assertFalse(result.hasNextPage)
    }

    @Test
    fun `PokemonListLoaded sets hasNextPage true when full page returned`() {
        val result = reducer.reduce(initial, PokemonListIntent.PokemonListLoaded(page))
        assertTrue(result.hasNextPage)
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
        val morePokemon = listOf(pokemon(2), pokemon(3))
        val result = reducer.reduce(existingState, PokemonListIntent.LoadMoreLoaded(morePokemon))
        assertEquals(3, result.pokemon.size)
        assertFalse(result.isLoadingMore)
        assertFalse(result.isRequestInFlight)
        assertEquals(3, result.currentOffset)
    }

    // ── SearchResultsLoaded ──────────────────────────────────────────────────

    @Test
    fun `SearchResultsLoaded updates searchResults`() {
        val results = listOf(pokemon(25))
        val result = reducer.reduce(initial, PokemonListIntent.SearchResultsLoaded(results))
        assertEquals(results, result.searchResults)
    }

    // ── UpdateFavorites ──────────────────────────────────────────────────────

    @Test
    fun `UpdateFavorites replaces favoriteIds set`() {
        val ids = setOf(1, 4, 7)
        val result = reducer.reduce(initial, PokemonListIntent.UpdateFavorites(ids))
        assertEquals(ids, result.favoriteIds)
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

    // ── displayList ──────────────────────────────────────────────────────────

    @Test
    fun `displayList returns pokemon when search is inactive`() {
        val state = initial.copy(
            pokemon = listOf(pokemon(1)),
            searchResults = listOf(pokemon(2)),
            isSearchActive = false,
        )
        assertEquals(state.pokemon, state.displayList)
    }

    @Test
    fun `displayList returns searchResults when search is active`() {
        val state = initial.copy(
            pokemon = listOf(pokemon(1)),
            searchResults = listOf(pokemon(2)),
            isSearchActive = true,
        )
        assertEquals(state.searchResults, state.displayList)
    }

    // ── No-op intents ────────────────────────────────────────────────────────

    @Test
    fun `OnPokemonClick does not change state`() {
        val result = reducer.reduce(initial, PokemonListIntent.OnPokemonClick(1))
        assertEquals(initial, result)
    }

    @Test
    fun `ToggleFavorite does not change state`() {
        val result = reducer.reduce(initial, PokemonListIntent.ToggleFavorite(pokemon(1)))
        assertEquals(initial, result)
    }
}
