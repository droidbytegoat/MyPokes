package com.souza.mypokes.data.local.db

import com.souza.mypokes.domain.model.Pokemon

fun PokemonEntity.toDomain() = Pokemon(id = id, name = name, imageUrl = imageUrl)
fun Pokemon.toPokemonEntity() = PokemonEntity(id = id, name = name, imageUrl = imageUrl)
