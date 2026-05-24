package com.mjalocha.rickandmortyapp.data.network

import com.mjalocha.rickandmortyapp.data.model.CharacterResponse
import com.mjalocha.rickandmortyapp.data.model.EpisodeResponse
import com.mjalocha.rickandmortyapp.data.model.Gender
import com.mjalocha.rickandmortyapp.data.model.Status
import com.mjalocha.rickandmortyapp.data.model.dto.CharacterDto
import com.mjalocha.rickandmortyapp.data.model.dto.EpisodeDto
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result
import com.mjalocha.rickandmortyapp.data.utils.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url
import org.koin.core.annotation.Singleton

@Singleton
class RickAndMortyApiImpl(
    private val httpClient: HttpClient
) : RickAndMortyApi {

    companion object {
        private const val BASE_URL = "https://rickandmortyapi.com/api"
    }

    override suspend fun getCharacters(
        page: Int,
        name: String?,
        status: Status?,
        gender: Gender?
    ): Result<CharacterResponse, DataError.Remote> {
        return safeCall<CharacterResponse> {
            httpClient.get {
                url("$BASE_URL/character")
                parameter("page", page)

                if (name != null) {
                    parameter("name", name.lowercase())
                }
                if (status != null) {
                    parameter("status", status.name.lowercase())
                }
                if (gender != null) {
                    parameter("gender", gender.name.lowercase())
                }
            }
        }
    }

    override suspend fun getCharacter(id: Int): Result<CharacterDto, DataError.Remote> {
        return safeCall<CharacterDto> {
            httpClient.get {
                url("$BASE_URL/character/$id")
            }
        }
    }


    override suspend fun getEpisode(id: Int): Result<EpisodeDto, DataError.Remote> {
        return safeCall<EpisodeDto> {
            httpClient.get {
                url("$BASE_URL/episode/$id")
            }
        }
    }

    override suspend fun getEpisodes(
        page: Int,
        name: String?,
        episodeCode: String?
    ): Result<EpisodeResponse, DataError.Remote> {
        return safeCall<EpisodeResponse> {
            httpClient.get {
                httpClient.get {
                    url("$BASE_URL/episode")
                    parameter("page", page)

                    if (name != null) {
                        parameter("name", name.lowercase())
                    }
                    if (episodeCode != null) {
                        parameter("episode", episodeCode.lowercase())
                    }
                }
            }
        }
    }

    override suspend fun getEpisodeById(
        ids: List<Int>
    ): Result<List<EpisodeDto>, DataError.Remote> {
        return safeCall<List<EpisodeDto>> {
            httpClient.get {
                url("$BASE_URL/episode/${ids.joinToString(",", postfix = ",")}")
            }
        }
    }

    override suspend fun getCharacterById(ids: List<Int>): Result<List<CharacterDto>, DataError.Remote> {
        return safeCall<List<CharacterDto>> {
            httpClient.get {
                url("$BASE_URL/character/${ids.joinToString(",", postfix = ",")}")
            }
        }
    }
}