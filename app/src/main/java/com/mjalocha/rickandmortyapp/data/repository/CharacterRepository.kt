package com.mjalocha.rickandmortyapp.data.repository

import com.mjalocha.rickandmortyapp.data.model.Gender
import com.mjalocha.rickandmortyapp.data.model.Status
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result
import com.mjalocha.rickandmortyapp.ui.models.CharacterDetails
import com.mjalocha.rickandmortyapp.ui.models.CharactersData

interface CharacterRepository {
    suspend fun getCharacters(
        page: Int = 1,
        name: String? = null,
        status: Status? = null,
        gender: Gender? = null,
    ): Result<CharactersData, DataError.Remote>

    suspend fun getCharacter(id: Int): Result<CharacterDetails, DataError.Remote>

    suspend fun getCharacterById(ids: List<Int>): Result<List<CharacterDetails>, DataError.Remote>
}
