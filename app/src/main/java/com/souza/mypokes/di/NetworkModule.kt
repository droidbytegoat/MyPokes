package com.souza.mypokes.di

import com.souza.mypokes.data.remote.api.PokemonApiClient
import com.souza.mypokes.data.remote.api.createHttpClient
import com.souza.mypokes.data.remote.datasource.RemotePokemonDataSource
import com.souza.mypokes.data.remote.datasource.RemotePokemonDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideHttpClient(): HttpClient = createHttpClient(OkHttp.create())

    @Provides
    @Singleton
    fun providePokemonApiClient(httpClient: HttpClient): PokemonApiClient =
        PokemonApiClient(httpClient)

    @Provides
    @Singleton
    fun provideRemotePokemonDataSource(
        apiClient: PokemonApiClient,
    ): RemotePokemonDataSource = RemotePokemonDataSourceImpl(apiClient)
}
