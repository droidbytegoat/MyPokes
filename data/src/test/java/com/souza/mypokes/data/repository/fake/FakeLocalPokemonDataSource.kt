package com.souza.mypokes.data.repository.fake

import com.souza.mypokes.data.local.datasource.LocalPokemonDataSource
import com.souza.mypokes.domain.model.Pokemon

class FakeLocalPokemonDataSource : LocalPokemonDataSource {

    private val pokemon = mutableListOf<Pokemon>()

    override suspend fun getAll(): List<Pokemon> = pokemon.toList()

    override suspend fun insertAll(pokemon: List<Pokemon>) {
        this.pokemon.addAll(pokemon)
    }

    override suspend fun deleteAll() {
        pokemon.clear()
    }
}
