package com.souza.mypokes.data.local.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pokemon_cache")
data class PokemonEntity(
    @PrimaryKey val id: Int,
    val name: String,
    val imageUrl: String,
)
