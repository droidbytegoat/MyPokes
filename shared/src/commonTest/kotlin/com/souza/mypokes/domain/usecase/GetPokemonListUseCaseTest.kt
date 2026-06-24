package com.souza.mypokes.domain.usecase

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.usecase.fake.FakePokemonRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetPokemonListUseCaseTest {

    private val repository = FakePokemonRepository()
    private val useCase = GetPokemonListUseCase(repository)

    @Test
    fun `invoke returns success with paginated list`() = runTest {
        val pokemon = (1..30).map { Pokemon(it, "pokemon$it", "url$it") }
        repository.pokemonList.addAll(pokemon)

        val result = useCase(offset = 0, limit = 20)

        assertTrue(result.isSuccess)
        assertEquals(20, result.getOrThrow().size)
        assertEquals(1, result.getOrThrow().first().id)
    }

    @Test
    fun `invoke returns second page correctly`() = runTest {
        val pokemon = (1..30).map { Pokemon(it, "pokemon$it", "url$it") }
        repository.pokemonList.addAll(pokemon)

        val result = useCase(offset = 20, limit = 20)

        assertTrue(result.isSuccess)
        assertEquals(10, result.getOrThrow().size)
        assertEquals(21, result.getOrThrow().first().id)
    }

    @Test
    fun `invoke uses default PAGE_SIZE`() = runTest {
        val pokemon = (1..25).map { Pokemon(it, "pokemon$it", "url$it") }
        repository.pokemonList.addAll(pokemon)

        val result = useCase(offset = 0)

        assertTrue(result.isSuccess)
        assertEquals(GetPokemonListUseCase.PAGE_SIZE, result.getOrThrow().size)
    }

    @Test
    fun `invoke returns empty list when no pokemon`() = runTest {
        val result = useCase(offset = 0)

        assertTrue(result.isSuccess)
        assertTrue(result.getOrThrow().isEmpty())
    }

    @Test
    fun `invoke returns failure when repository fails`() = runTest {
        repository.shouldFail = true

        val result = useCase(offset = 0)

        assertTrue(result.isFailure)
        assertEquals(repository.errorMessage, result.exceptionOrNull()?.message)
    }
}
