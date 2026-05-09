package com.mjalocha.rickandmortyapp.data.network

import com.mjalocha.rickandmortyapp.data.model.CharacterResponse
import com.mjalocha.rickandmortyapp.data.model.Gender
import com.mjalocha.rickandmortyapp.data.model.Status
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result

interface RickAndMortyApi {
    suspend fun getCharacters(
        page: Int = 1,
        status: Status? = null,
        gender: Gender? = null
    ): Result<CharacterResponse, DataError.Remote>
}