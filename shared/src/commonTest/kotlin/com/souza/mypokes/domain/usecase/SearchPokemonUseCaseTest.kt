package com.souza.mypokes.domain.usecase

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.usecase.fake.FakePokemonRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchPokemonUseCaseTest {

    private val repository = FakePokemonRepository()
    private val useCase = SearchPokemonUseCase(repository)

    @Test
    fun `invoke returns matching pokemon`() = runTest {
        repository.addPokemon(
            Pokemon(1, "bulbasaur", "url1"),
            Pokemon(2, "ivysaur", "url2"),
            Pokemon(3, "charmander", "url3"),
        )

        val result = useCase("saur")

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrThrow().size)
    }

    @Test
    fun `invoke is case insensitive`() = runTest {
        repository.addPokemon(Pokemon(1, "Bulbasaur", "url1"))

        val result = useCase("bulba")

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
    }

    @Test
    fun `invoke trims whitespace from query`() = runTest {
        repository.addPokemon(Pokemon(1, "bulbasaur", "url1"))

        val result = useCase("  bulbasaur  ")

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
    }

    @Test
    fun `invoke returns empty list when no match`() = runTest {
        repository.addPokemon(Pokemon(1, "bulbasaur", "url1"))

        val result = useCase("pikachu")

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun `invoke returns failure when repository fails`() = runTest {
        repository.shouldFail = true

        val result = useCase("bulbasaur")

        assertTrue(result.isFailure)
        assertEquals(repository.errorMessage, result.exceptionOrNull()?.message)
    }
}
