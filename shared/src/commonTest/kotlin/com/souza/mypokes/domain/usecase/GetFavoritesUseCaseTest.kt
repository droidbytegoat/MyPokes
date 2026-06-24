package com.souza.mypokes.domain.usecase

import app.cash.turbine.test
import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.usecase.fake.FakeFavoritesRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetFavoritesUseCaseTest {

    private val repository = FakeFavoritesRepository()
    private val useCase = GetFavoritesUseCase(repository)

    @Test
    fun `invoke emits empty list when no favorites`() = runTest {
        useCase().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke emits current favorites`() = runTest {
        val bulbasaur = Pokemon(1, "bulbasaur", "url1")
        val charmander = Pokemon(4, "charmander", "url4")
        repository.setFavorites(bulbasaur, charmander)

        useCase().test {
            val favorites = awaitItem()
            assertEquals(2, favorites.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke emits updated list when favorites change`() = runTest {
        val bulbasaur = Pokemon(1, "bulbasaur", "url1")

        useCase().test {
            assertTrue(awaitItem().isEmpty())

            repository.setFavorites(bulbasaur)

            val updated = awaitItem()
            assertEquals(1, updated.size)
            assertEquals("bulbasaur", updated.first().name)

            cancelAndIgnoreRemainingEvents()
        }
    }
}
