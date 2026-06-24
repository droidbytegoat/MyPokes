package com.souza.mypokes.data.repository

import app.cash.turbine.test
import com.souza.mypokes.data.repository.fake.FakeLocalFavoritesDataSource
import com.souza.mypokes.domain.model.Pokemon
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FavoritesRepositoryImplTest {

    private val localDataSource = FakeLocalFavoritesDataSource()
    private val repository = FavoritesRepositoryImpl(localDataSource)

    private val bulbasaur = Pokemon(1, "Bulbasaur", "url1")
    private val charmander = Pokemon(4, "Charmander", "url4")

    @Test
    fun `getFavorites returns empty list initially`() = runTest {
        repository.getFavorites().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `getFavorites reflects local data source`() = runTest {
        localDataSource.setFavorites(bulbasaur, charmander)

        repository.getFavorites().test {
            assertEquals(2, awaitItem().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFavorite adds pokemon when not in favorites`() = runTest {
        repository.toggleFavorite(bulbasaur)

        repository.getFavorites().test {
            val favorites = awaitItem()
            assertEquals(1, favorites.size)
            assertEquals(bulbasaur, favorites.first())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFavorite removes pokemon when already in favorites`() = runTest {
        localDataSource.setFavorites(bulbasaur)

        repository.toggleFavorite(bulbasaur)

        repository.getFavorites().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFavorite preserves other favorites when removing`() = runTest {
        localDataSource.setFavorites(bulbasaur, charmander)

        repository.toggleFavorite(bulbasaur)

        repository.getFavorites().test {
            val favorites = awaitItem()
            assertEquals(1, favorites.size)
            assertEquals(charmander, favorites.first())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `toggleFavorite called twice results in no change`() = runTest {
        repository.toggleFavorite(bulbasaur)
        repository.toggleFavorite(bulbasaur)

        repository.getFavorites().test {
            assertTrue(awaitItem().isEmpty())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
