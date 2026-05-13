package com.mjalocha.rickandmortyapp.data.network

import com.mjalocha.rickandmortyapp.data.model.CharacterResponse
import com.mjalocha.rickandmortyapp.data.model.Gender
import com.mjalocha.rickandmortyapp.data.model.Status
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result
import com.mjalocha.rickandmortyapp.data.utils.safeCall
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.url

class RickAndMortyApiImpl(
    private val httpClient: HttpClient
) : RickAndMortyApi {
    override suspend fun getCharacters(
        page: Int,
        name: String?,
        status: Status?,
        gender: Gender?
    ): Result<CharacterResponse, DataError.Remote> {
        return safeCall<CharacterResponse> {
            httpClient.get {
                url("https://rickandmortyapi.com/api/character")
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
}