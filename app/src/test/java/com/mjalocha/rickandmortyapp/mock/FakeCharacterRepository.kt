package com.mjalocha.rickandmortyapp.mock

import com.mjalocha.rickandmortyapp.data.repository.CharacterRepository
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result
import com.mjalocha.rickandmortyapp.ui.models.CharacterDetails
import com.mjalocha.rickandmortyapp.ui.models.CharactersData

class FakeCharacterRepository(
    var defaultResult: Result<CharactersData, DataError.Remote> =
        Result.Success(CharactersData(nextPage = null, results = emptyList())),
    var getCharacterResult: Result<CharacterDetails, DataError.Remote> = Result.Error(DataError.Remote.UNKNOWN),
    var getCharacterByIdResult: Result<List<CharacterDetails>, DataError.Remote> =
        Result.Success(
            emptyList(),
        ),
) : CharacterRepository {
    data class GetCharactersCall(
        val page: Int,
        val name: String?,
        val status: String?,
        val gender: String?,
    )

    val resultByName: MutableMap<String?, Result<CharactersData, DataError.Remote>> =
        mutableMapOf()
    val getCharactersCalls: MutableList<GetCharactersCall> = mutableListOf()
    val requestedCharacterIds: MutableList<Int> = mutableListOf()
    val requestedCharacterIdLists: MutableList<List<Int>> = mutableListOf()

    override suspend fun getCharacters(
        page: Int,
        name: String?,
        status: String?,
        gender: String?,
    ): Result<CharactersData, DataError.Remote> {
        getCharactersCalls += GetCharactersCall(page, name, status, gender)
        return resultByName[name] ?: defaultResult
    }

    override suspend fun getCharacter(id: Int): Result<CharacterDetails, DataError.Remote> {
        requestedCharacterIds += id
        return getCharacterResult
    }

    override suspend fun getCharacterById(ids: List<Int>): Result<List<CharacterDetails>, DataError.Remote> {
        requestedCharacterIdLists += ids
        return getCharacterByIdResult
    }

    fun clearCalls() {
        getCharactersCalls.clear()
    }
}
