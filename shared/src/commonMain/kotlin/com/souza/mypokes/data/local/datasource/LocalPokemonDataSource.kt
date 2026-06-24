package com.souza.mypokes.data.local.datasource

import com.souza.mypokes.domain.model.Pokemon

interface LocalPokemonDataSource {
    suspend fun getAll(): List<Pokemon>
    suspend fun insertAll(pokemon: List<Pokemon>)
    suspend fun deleteAll()
}
