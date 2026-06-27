package com.souza.mypokes.domain.usecase

import app.cash.turbine.test
import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.usecase.fake.FakeFavoritesRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ToggleFavoriteUseCaseTest {

    private val repository = FakeFavoritesRepository()
    private val useCase = ToggleFavoriteUseCase(repository)

    @Test
    fun `invoke adds pokemon when not in favorites`() = runTest {
        val bulbasaur = Pokemon(1, "bulbasaur", "url1")

        useCase(bulbasaur)

        repository.getFavorites().test {
            val favorites = awaitItem()
            assertEquals(1, favorites.size)
            assertEquals(bulbasaur, favorites.first())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke removes pokemon when already in favorites`() = runTest {
        val bulbasaur = Pokemon(1, "bulbasaur", "url1")
        repository.setFavorites(bulbasaur)

        useCase(bulbasaur)

        repository.getFavorites().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke toggles correctly on repeated calls`() = runTest {
        val bulbasaur = Pokemon(1, "bulbasaur", "url1")

        useCase(bulbasaur)
        useCase(bulbasaur)

        repository.getFavorites().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke preserves other favorites when removing`() = runTest {
        val bulbasaur = Pokemon(1, "bulbasaur", "url1")
        val charmander = Pokemon(4, "charmander", "url4")
        repository.setFavorites(bulbasaur, charmander)

        useCase(bulbasaur)

        repository.getFavorites().test {
            val favorites = awaitItem()
            assertEquals(1, favorites.size)
            assertEquals(charmander, favorites.first())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
