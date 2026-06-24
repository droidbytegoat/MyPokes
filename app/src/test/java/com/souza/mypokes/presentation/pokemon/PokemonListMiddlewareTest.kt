package com.souza.mypokes.presentation.pokemon

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.usecase.GetPokemonListUseCase
import com.souza.mypokes.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PokemonListMiddlewareTest {

    private val getPokemonListUseCase: GetPokemonListUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()

    private lateinit var middleware: PokemonListMiddleware

    private val dispatched = mutableListOf<PokemonListIntent>()
    private val effects = mutableListOf<PokemonListEffect>()

    private val dispatch: (PokemonListIntent) -> Unit = { dispatched.add(it) }
    private val sendEffect: (PokemonListEffect) -> Unit = { effects.add(it) }

    private fun pokemon(id: Int) = Pokemon(id = id, name = "pokemon-$id", imageUrl = "url-$id")
    private val page = (1..GetPokemonListUseCase.PAGE_SIZE).map { pokemon(it) }

    @Before
    fun setup() {
        middleware = PokemonListMiddleware(getPokemonListUseCase, toggleFavoriteUseCase)
        dispatched.clear()
        effects.clear()
    }

    // ── LoadInitial ──────────────────────────────────────────────────────────

    @Test
    fun `LoadInitial fetches first page and dispatches PokemonListLoaded on success`() = runTest {
        coEvery { getPokemonListUseCase(0) } returns Result.success(page)

        middleware.process(PokemonListIntent.LoadInitial, PokemonListState(), dispatch, sendEffect)

        val intent = dispatched.single() as PokemonListIntent.PokemonListLoaded
        assertEquals(page, intent.pokemon)
    }

    @Test
    fun `LoadInitial dispatches LoadFailed on error`() = runTest {
        coEvery { getPokemonListUseCase(0) } returns Result.failure(RuntimeException("timeout"))

        middleware.process(PokemonListIntent.LoadInitial, PokemonListState(), dispatch, sendEffect)

        val intent = dispatched.single() as PokemonListIntent.LoadFailed
        assertEquals("timeout", intent.message)
    }

    // ── Refresh ──────────────────────────────────────────────────────────────

    @Test
    fun `Refresh fetches first page and dispatches PokemonListLoaded`() = runTest {
        coEvery { getPokemonListUseCase(0) } returns Result.success(page)

        middleware.process(PokemonListIntent.Refresh, PokemonListState(), dispatch, sendEffect)

        assertTrue(dispatched.single() is PokemonListIntent.PokemonListLoaded)
    }

    // ── LoadNextPage ─────────────────────────────────────────────────────────

    @Test
    fun `LoadNextPage fetches next page when not in flight and has next page`() = runTest {
        val state = PokemonListState(
            isRequestInFlight = false,
            hasNextPage = true,
            currentOffset = 20,
            isSearchActive = false,
        )
        coEvery { getPokemonListUseCase(20) } returns Result.success(page)

        middleware.process(PokemonListIntent.LoadNextPage, state, dispatch, sendEffect)

        val intent = dispatched.single() as PokemonListIntent.LoadMoreLoaded
        assertEquals(page, intent.pokemon)
    }

    @Test
    fun `LoadNextPage is skipped when already in flight`() = runTest {
        val state = PokemonListState(isRequestInFlight = true, hasNextPage = true)

        middleware.process(PokemonListIntent.LoadNextPage, state, dispatch, sendEffect)

        assertTrue(dispatched.isEmpty())
        coVerify(exactly = 0) { getPokemonListUseCase(any()) }
    }

    @Test
    fun `LoadNextPage is skipped when no next page`() = runTest {
        val state = PokemonListState(isRequestInFlight = false, hasNextPage = false)

        middleware.process(PokemonListIntent.LoadNextPage, state, dispatch, sendEffect)

        assertTrue(dispatched.isEmpty())
        coVerify(exactly = 0) { getPokemonListUseCase(any()) }
    }

    @Test
    fun `LoadNextPage is skipped when search is active`() = runTest {
        val state = PokemonListState(
            isRequestInFlight = false,
            hasNextPage = true,
            isSearchActive = true,
        )

        middleware.process(PokemonListIntent.LoadNextPage, state, dispatch, sendEffect)

        assertTrue(dispatched.isEmpty())
        coVerify(exactly = 0) { getPokemonListUseCase(any()) }
    }

    @Test
    fun `LoadNextPage dispatches LoadFailed on error`() = runTest {
        val state = PokemonListState(isRequestInFlight = false, hasNextPage = true, currentOffset = 20)
        coEvery { getPokemonListUseCase(20) } returns Result.failure(RuntimeException("no internet"))

        middleware.process(PokemonListIntent.LoadNextPage, state, dispatch, sendEffect)

        val intent = dispatched.single() as PokemonListIntent.LoadFailed
        assertEquals("no internet", intent.message)
    }

    // ── ToggleFavorite ───────────────────────────────────────────────────────

    @Test
    fun `ToggleFavorite calls toggleFavoriteUseCase`() = runTest {
        val poke = pokemon(25)
        coEvery { toggleFavoriteUseCase(poke) } returns Unit

        middleware.process(PokemonListIntent.ToggleFavorite(poke), PokemonListState(), dispatch, sendEffect)

        coVerify(exactly = 1) { toggleFavoriteUseCase(poke) }
        assertTrue(dispatched.isEmpty())
        assertTrue(effects.isEmpty())
    }

    // ── OnPokemonClick ───────────────────────────────────────────────────────

    @Test
    fun `OnPokemonClick sends NavigateToDetail effect`() = runTest {
        middleware.process(PokemonListIntent.OnPokemonClick(42), PokemonListState(), dispatch, sendEffect)

        val effect = effects.single() as PokemonListEffect.NavigateToDetail
        assertEquals(42, effect.pokemonId)
        assertTrue(dispatched.isEmpty())
    }

    // ── Other intents ────────────────────────────────────────────────────────

    @Test
    fun `Search intent produces no side effects`() = runTest {
        middleware.process(PokemonListIntent.Search("pikachu"), PokemonListState(), dispatch, sendEffect)

        assertTrue(dispatched.isEmpty())
        assertTrue(effects.isEmpty())
    }

    @Test
    fun `ClearSearch intent produces no side effects`() = runTest {
        middleware.process(PokemonListIntent.ClearSearch, PokemonListState(), dispatch, sendEffect)

        assertTrue(dispatched.isEmpty())
        assertTrue(effects.isEmpty())
    }
}
