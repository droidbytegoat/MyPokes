package com.souza.mypokes.di

import com.souza.mypokes.data.local.datasource.LocalFavoritesDataSource
import com.souza.mypokes.data.local.datasource.RoomFavoritesDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindLocalFavoritesDataSource(
        impl: RoomFavoritesDataSource,
    ): LocalFavoritesDataSource
}
