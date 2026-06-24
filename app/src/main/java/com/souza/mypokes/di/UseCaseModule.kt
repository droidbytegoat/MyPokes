package com.souza.mypokes.di

import com.souza.mypokes.domain.repository.FavoritesRepository
import com.souza.mypokes.domain.repository.PokemonRepository
import com.souza.mypokes.domain.usecase.GetFavoritesUseCase
import com.souza.mypokes.domain.usecase.GetPokemonDetailUseCase
import com.souza.mypokes.domain.usecase.GetPokemonListUseCase
import com.souza.mypokes.domain.usecase.SearchPokemonUseCase
import com.souza.mypokes.domain.usecase.ToggleFavoriteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetPokemonListUseCase(repository: PokemonRepository): GetPokemonListUseCase =
        GetPokemonListUseCase(repository)

    @Provides
    @Singleton
    fun provideGetPokemonDetailUseCase(repository: PokemonRepository): GetPokemonDetailUseCase =
        GetPokemonDetailUseCase(repository)

    @Provides
    @Singleton
    fun provideSearchPokemonUseCase(repository: PokemonRepository): SearchPokemonUseCase =
        SearchPokemonUseCase(repository)

    @Provides
    @Singleton
    fun provideGetFavoritesUseCase(repository: FavoritesRepository): GetFavoritesUseCase =
        GetFavoritesUseCase(repository)

    @Provides
    @Singleton
    fun provideToggleFavoriteUseCase(repository: FavoritesRepository): ToggleFavoriteUseCase =
        ToggleFavoriteUseCase(repository)
}
