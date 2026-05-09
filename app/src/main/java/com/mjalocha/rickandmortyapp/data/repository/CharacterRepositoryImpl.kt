package com.mjalocha.rickandmortyapp.data.repository

import com.mjalocha.rickandmortyapp.data.model.CharacterResponse
import com.mjalocha.rickandmortyapp.data.model.Gender
import com.mjalocha.rickandmortyapp.data.model.Status
import com.mjalocha.rickandmortyapp.data.network.RickAndMortyApi
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result

class CharacterRepositoryImpl(
    private val apiClient: RickAndMortyApi
) : CharacterRepository {
    override suspend fun getCharacters(
        page: Int,
        status: Status?,
        gender: Gender?
    ): Result<CharacterResponse, DataError.Remote> {
        return apiClient.getCharacters(
            page,
            status,
            gender
        )
    }
}