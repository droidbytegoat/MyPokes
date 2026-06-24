package com.souza.mypokes.presentation.detail

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.model.PokemonDetail
import com.souza.mypokes.domain.model.PokemonStat
import com.souza.mypokes.domain.model.PokemonType
import com.souza.mypokes.domain.usecase.GetPokemonDetailUseCase
import com.souza.mypokes.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PokemonDetailMiddlewareTest {

    private val getPokemonDetailUseCase: GetPokemonDetailUseCase = mockk()
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()

    private lateinit var middleware: PokemonDetailMiddleware

    private val dispatched = mutableListOf<PokemonDetailIntent>()
    private val effects = mutableListOf<PokemonDetailEffect>()

    private val dispatch: (PokemonDetailIntent) -> Unit = { dispatched.add(it) }
    private val sendEffect: (PokemonDetailEffect) -> Unit = { effects.add(it) }

    private val fakeDetail = PokemonDetail(
        id = 1,
        name = "bulbasaur",
        imageUrl = "https://example.com/1.png",
        types = listOf(PokemonType("grass", 1)),
        height = 7,
        weight = 69,
        stats = listOf(PokemonStat("hp", 45)),
    )

    @Before
    fun setup() {
        middleware = PokemonDetailMiddleware(getPokemonDetailUseCase, toggleFavoriteUseCase)
        dispatched.clear()
        effects.clear()
    }

    // ── LoadDetail ───────────────────────────────────────────────────────────

    @Test
    fun `LoadDetail dispatches DetailLoaded on success`() = runTest {
        coEvery { getPokemonDetailUseCase(1) } returns Result.success(fakeDetail)

        middleware.process(
            PokemonDetailIntent.LoadDetail(1),
            PokemonDetailState(),
            dispatch,
            sendEffect,
        )

        val intent = dispatched.single() as PokemonDetailIntent.DetailLoaded
        assertEquals(fakeDetail, intent.pokemon)
        assertTrue(effects.isEmpty())
    }

    @Test
    fun `LoadDetail dispatches LoadFailed on error`() = runTest {
        coEvery { getPokemonDetailUseCase(1) } returns Result.failure(RuntimeException("timeout"))

        middleware.process(
            PokemonDetailIntent.LoadDetail(1),
            PokemonDetailState(),
            dispatch,
            sendEffect,
        )

        val intent = dispatched.single() as PokemonDetailIntent.LoadFailed
        assertEquals("timeout", intent.message)
        assertTrue(effects.isEmpty())
    }

    @Test
    fun `LoadDetail dispatches LoadFailed with fallback message when exception has no message`() = runTest {
        coEvery { getPokemonDetailUseCase(99) } returns Result.failure(RuntimeException())

        middleware.process(
            PokemonDetailIntent.LoadDetail(99),
            PokemonDetailState(),
            dispatch,
            sendEffect,
        )

        val intent = dispatched.single() as PokemonDetailIntent.LoadFailed
        assertEquals("Failed to load Pokémon", intent.message)
    }

    // ── ToggleFavorite ───────────────────────────────────────────────────────

    @Test
    fun `ToggleFavorite calls toggleFavoriteUseCase with mapped Pokemon`() = runTest {
        val expectedPokemon = Pokemon(id = 1, name = "bulbasaur", imageUrl = "https://example.com/1.png")
        coEvery { toggleFavoriteUseCase(expectedPokemon) } returns Unit

        middleware.process(
            PokemonDetailIntent.ToggleFavorite,
            PokemonDetailState(pokemon = fakeDetail),
            dispatch,
            sendEffect,
        )

        coVerify(exactly = 1) { toggleFavoriteUseCase(expectedPokemon) }
        assertTrue(dispatched.isEmpty())
        assertTrue(effects.isEmpty())
    }

    @Test
    fun `ToggleFavorite is skipped when pokemon is null`() = runTest {
        middleware.process(
            PokemonDetailIntent.ToggleFavorite,
            PokemonDetailState(pokemon = null),
            dispatch,
            sendEffect,
        )

        coVerify(exactly = 0) { toggleFavoriteUseCase(any()) }
        assertTrue(dispatched.isEmpty())
        assertTrue(effects.isEmpty())
    }

    // ── NavigateBack ─────────────────────────────────────────────────────────

    @Test
    fun `NavigateBack sends NavigateBack effect`() = runTest {
        middleware.process(
            PokemonDetailIntent.NavigateBack,
            PokemonDetailState(),
            dispatch,
            sendEffect,
        )

        val effect = effects.single()
        assertEquals(PokemonDetailEffect.NavigateBack, effect)
        assertTrue(dispatched.isEmpty())
    }

    // ── Other intents ────────────────────────────────────────────────────────

    @Test
    fun `DetailLoaded intent produces no side effects`() = runTest {
        middleware.process(
            PokemonDetailIntent.DetailLoaded(fakeDetail),
            PokemonDetailState(),
            dispatch,
            sendEffect,
        )

        assertTrue(dispatched.isEmpty())
        assertTrue(effects.isEmpty())
    }

    @Test
    fun `FavoriteStatusLoaded intent produces no side effects`() = runTest {
        middleware.process(
            PokemonDetailIntent.FavoriteStatusLoaded(true),
            PokemonDetailState(),
            dispatch,
            sendEffect,
        )

        assertTrue(dispatched.isEmpty())
        assertTrue(effects.isEmpty())
    }
}
