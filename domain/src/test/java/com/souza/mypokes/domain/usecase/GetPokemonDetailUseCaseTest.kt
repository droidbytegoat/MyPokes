package com.souza.mypokes.domain.usecase

import com.souza.mypokes.domain.model.Pokemon
import com.souza.mypokes.domain.usecase.fake.FakePokemonRepository
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetPokemonDetailUseCaseTest {

    private val repository = FakePokemonRepository()
    private val useCase = GetPokemonDetailUseCase(repository)

    @Test
    fun `invoke returns detail for existing pokemon`() = runTest {
        repository.addPokemon(Pokemon(1, "bulbasaur", "url1"))

        val result = useCase(id = 1)

        assertTrue(result.isSuccess)
        val detail = result.getOrThrow()
        assertEquals(1, detail.id)
        assertEquals("bulbasaur", detail.name)
    }

    @Test
    fun `invoke returns failure for non-existent pokemon`() = runTest {
        val result = useCase(id = 999)

        assertTrue(result.isFailure)
    }

    @Test
    fun `invoke returns failure when repository fails`() = runTest {
        repository.addPokemon(Pokemon(1, "bulbasaur", "url1"))
        repository.shouldFail = true

        val result = useCase(id = 1)

        assertTrue(result.isFailure)
        assertEquals(repository.errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `invoke returns correct detail fields`() = runTest {
        repository.addPokemon(Pokemon(4, "charmander", "url4"))

        val result = useCase(id = 4)

        assertTrue(result.isSuccess)
        val detail = result.getOrThrow()
        assertEquals(4, detail.id)
        assertEquals("charmander", detail.name)
        assertTrue(detail.types.isNotEmpty())
        assertTrue(detail.stats.isNotEmpty())
    }
}
