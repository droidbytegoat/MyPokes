package com.souza.mypokes.presentation.favorites

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.usecase.ToggleFavoriteUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FavoritesMiddlewareTest {

    private val toggleFavoriteUseCase: ToggleFavoriteUseCase = mockk()

    private lateinit var middleware: FavoritesMiddleware

    private val dispatched = mutableListOf<FavoritesIntent>()
    private val effects = mutableListOf<FavoritesEffect>()

    private val dispatch: (FavoritesIntent) -> Unit = { dispatched.add(it) }
    private val sendEffect: (FavoritesEffect) -> Unit = { effects.add(it) }

    private fun pokemon(id: Int) = Pokemon(id = id, name = "pokemon-$id", imageUrl = "url-$id")

    @Before
    fun setup() {
        middleware = FavoritesMiddleware(toggleFavoriteUseCase)
        dispatched.clear()
        effects.clear()
    }

    // ── OnPokemonClick ───────────────────────────────────────────────────────

    @Test
    fun `OnPokemonClick sends NavigateToDetail effect`() = runTest {
        middleware.process(FavoritesIntent.OnPokemonClick(25), FavoritesState(), dispatch, sendEffect)

        val effect = effects.single() as FavoritesEffect.NavigateToDetail
        assertEquals(25, effect.pokemonId)
        assertTrue(dispatched.isEmpty())
    }

    // ── RemoveFavorite ───────────────────────────────────────────────────────

    @Test
    fun `RemoveFavorite calls toggleFavoriteUseCase`() = runTest {
        val poke = pokemon(4)
        coEvery { toggleFavoriteUseCase(poke) } returns Unit

        middleware.process(FavoritesIntent.RemoveFavorite(poke), FavoritesState(), dispatch, sendEffect)

        coVerify(exactly = 1) { toggleFavoriteUseCase(poke) }
        assertTrue(dispatched.isEmpty())
        assertTrue(effects.isEmpty())
    }

    // ── FavoritesLoaded ──────────────────────────────────────────────────────

    @Test
    fun `FavoritesLoaded produces no side effects`() = runTest {
        middleware.process(
            FavoritesIntent.FavoritesLoaded(listOf(pokemon(1))),
            FavoritesState(),
            dispatch,
            sendEffect,
        )

        assertTrue(dispatched.isEmpty())
        assertTrue(effects.isEmpty())
    }
}
