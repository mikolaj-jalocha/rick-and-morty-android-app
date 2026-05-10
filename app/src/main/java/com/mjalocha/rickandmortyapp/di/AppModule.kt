package com.mjalocha.rickandmortyapp.di

import com.mjalocha.rickandmortyapp.data.network.RickAndMortyApi
import com.mjalocha.rickandmortyapp.data.network.RickAndMortyApiImpl
import com.mjalocha.rickandmortyapp.data.repository.CharacterRepository
import com.mjalocha.rickandmortyapp.data.repository.CharacterRepositoryImpl
import com.mjalocha.rickandmortyapp.ui.characters_screen.CharactersViewModel
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.android.annotation.KoinViewModel
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
@ComponentScan("com.mjalocha")
class AppModule {

    @Single
    fun httpClient(): HttpClient = HttpClient(OkHttp) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
            })
        }
    }

    @Single(binds = [RickAndMortyApi::class])
    fun ktorDataSource(httpClient: HttpClient) = RickAndMortyApiImpl(httpClient)

    @Single(binds = [CharacterRepository::class])
    fun charactersRepository(apiClient: RickAndMortyApi) = CharacterRepositoryImpl(apiClient)

    @KoinViewModel
    fun charactersViewModel(
        characterRepository: CharacterRepository
    ) = CharactersViewModel(characterRepository)

}