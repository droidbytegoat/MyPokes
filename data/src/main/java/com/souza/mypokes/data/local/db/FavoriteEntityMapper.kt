package com.souza.mypokes.data.local.db

import com.souza.mypokes.domain.model.Pokemon

fun FavoriteEntity.toDomain() = Pokemon(id = id, name = name, imageUrl = imageUrl)

fun Pokemon.toEntity() = FavoriteEntity(id = id, name = name, imageUrl = imageUrl)
