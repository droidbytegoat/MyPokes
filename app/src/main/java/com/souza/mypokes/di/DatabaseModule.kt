package com.souza.mypokes.di

import android.content.Context
import androidx.room.Room
import com.souza.mypokes.data.local.db.AppDatabase
import com.souza.mypokes.data.local.db.FavoriteDao
import com.souza.mypokes.data.local.db.PokemonDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "mypokes.db",
        ).fallbackToDestructiveMigration(dropAllTables = true).build()

    @Provides
    fun provideFavoriteDao(database: AppDatabase): FavoriteDao = database.favoriteDao()

    @Provides
    fun providePokemonDao(database: AppDatabase): PokemonDao = database.pokemonDao()
}
