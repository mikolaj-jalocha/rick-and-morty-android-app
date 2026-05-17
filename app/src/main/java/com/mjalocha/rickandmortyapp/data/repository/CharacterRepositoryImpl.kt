package com.mjalocha.rickandmortyapp.data.repository

import com.mjalocha.rickandmortyapp.data.model.CharacterResponse
import com.mjalocha.rickandmortyapp.data.model.Gender
import com.mjalocha.rickandmortyapp.data.model.Status
import com.mjalocha.rickandmortyapp.data.model.dto.CharacterDto
import com.mjalocha.rickandmortyapp.data.network.RickAndMortyApi
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result
import org.koin.core.annotation.Singleton

@Singleton
class CharacterRepositoryImpl(
    private val apiClient: RickAndMortyApi
) : CharacterRepository {
    override suspend fun getCharacters(
        page: Int,
        name: String?,
        status: Status?,
        gender: Gender?
    ): Result<CharacterResponse, DataError.Remote> {
        return apiClient.getCharacters(
            page,
            name,
            status,
            gender
        )
    }

    override suspend fun getCharacter(id: Int): Result<CharacterDto, DataError.Remote> {
        return apiClient.getCharacter(id)
    }
}