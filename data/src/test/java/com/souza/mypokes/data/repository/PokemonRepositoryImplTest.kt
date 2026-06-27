package com.souza.mypokes.data.repository

import com.souza.mypokes.data.repository.fake.FakeLocalPokemonDataSource
import com.souza.mypokes.data.repository.fake.FakeRemotePokemonDataSource
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PokemonRepositoryImplTest {

    private val remoteDataSource = FakeRemotePokemonDataSource()
    private val localDataSource = FakeLocalPokemonDataSource()
    private val repository = PokemonRepositoryImpl(remoteDataSource, localDataSource)

    @Test
    fun `getPokemonList returns success with mapped list`() = runTest {
        remoteDataSource.addPokemon(id = 1, name = "bulbasaur")
        remoteDataSource.addPokemon(id = 4, name = "charmander")

        val result = repository.getPokemonList(offset = 0, limit = 20)

        assertTrue(result.isSuccess)
        val list = result.getOrThrow()
        assertEquals(2, list.size)
        assertEquals(1, list[0].id)
        assertEquals("Bulbasaur", list[0].name)
    }

    @Test
    fun `getPokemonList returns failure on network error`() = runTest {
        remoteDataSource.shouldFail = true

        val result = repository.getPokemonList(offset = 0, limit = 20)

        assertTrue(result.isFailure)
        assertEquals(remoteDataSource.errorMessage, result.exceptionOrNull()?.message)
    }

    @Test
    fun `getPokemonList respects offset and limit`() = runTest {
        repeat(5) { remoteDataSource.addPokemon(id = it + 1, name = "pokemon${it + 1}") }

        val result = repository.getPokemonList(offset = 2, limit = 2)

        assertTrue(result.isSuccess)
        assertEquals(2, result.getOrThrow().size)
        assertEquals(3, result.getOrThrow().first().id)
    }

    @Test
    fun `getPokemonDetail returns success with mapped detail`() = runTest {
        remoteDataSource.addPokemon(id = 1, name = "bulbasaur")

        val result = repository.getPokemonDetail(id = 1)

        assertTrue(result.isSuccess)
        val detail = result.getOrThrow()
        assertEquals(1, detail.id)
        assertEquals("Bulbasaur", detail.name)
        assertTrue(detail.types.isNotEmpty())
        assertTrue(detail.stats.isNotEmpty())
    }

    @Test
    fun `getPokemonDetail returns failure for non-existent id`() = runTest {
        val result = repository.getPokemonDetail(id = 999)

        assertTrue(result.isFailure)
    }

    @Test
    fun `getPokemonDetail returns failure on network error`() = runTest {
        remoteDataSource.addPokemon(id = 1, name = "bulbasaur")
        remoteDataSource.shouldFail = true

        val result = repository.getPokemonDetail(id = 1)

        assertTrue(result.isFailure)
    }

    @Test
    fun `searchPokemon returns success with found pokemon`() = runTest {
        remoteDataSource.addPokemon(id = 1, name = "bulbasaur")

        val result = repository.searchPokemon("bulbasaur")

        assertTrue(result.isSuccess)
        assertEquals(1, result.getOrThrow().size)
        assertEquals(1, result.getOrThrow().first().id)
    }

    @Test
    fun `searchPokemon returns failure when pokemon not found`() = runTest {
        val result = repository.searchPokemon("unknownmon")

        assertTrue(result.isFailure)
    }

    @Test
    fun `searchPokemon returns failure on network error`() = runTest {
        remoteDataSource.shouldFail = true

        val result = repository.searchPokemon("bulbasaur")

        assertTrue(result.isFailure)
    }
}
