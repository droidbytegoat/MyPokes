package com.souza.mypokes.di

import com.souza.mypokes.data.local.datasource.LocalFavoritesDataSource
import com.souza.mypokes.data.remote.datasource.RemotePokemonDataSource
import com.souza.mypokes.data.repository.FavoritesRepositoryImpl
import com.souza.mypokes.data.repository.PokemonRepositoryImpl
import com.souza.mypokes.domain.repository.FavoritesRepository
import com.souza.mypokes.domain.repository.PokemonRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun providePokemonRepository(
        remoteDataSource: RemotePokemonDataSource,
    ): PokemonRepository = PokemonRepositoryImpl(remoteDataSource)

    @Provides
    @Singleton
    fun provideFavoritesRepository(
        localDataSource: LocalFavoritesDataSource,
    ): FavoritesRepository = FavoritesRepositoryImpl(localDataSource)
}
