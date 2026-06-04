package com.mjalocha.rickandmortyapp.mock

import com.mjalocha.rickandmortyapp.data.model.Gender
import com.mjalocha.rickandmortyapp.data.model.Status
import com.mjalocha.rickandmortyapp.data.repository.CharacterRepository
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result
import com.mjalocha.rickandmortyapp.ui.models.CharacterDetails
import com.mjalocha.rickandmortyapp.ui.models.CharactersData
import kotlinx.coroutines.delay

class FakeCharacterRepository(
    var defaultResult: Result<CharactersData, DataError.Remote> =
        Result.Success(CharactersData(nextPage = null, results = emptyList())),
    var getCharacterResult: Result<CharacterDetails, DataError.Remote> = Result.Error(DataError.Remote.UNKNOWN),
    var getCharacterByIdResult: Result<List<CharacterDetails>, DataError.Remote> = Result.Success(emptyList()),
    var getCharactersDelayMillis: Long = 0L,
    var getCharacterDelayMillis: Long = 0L,
    var getCharacterByIdDelayMillis: Long = 0L,
) : CharacterRepository {
    data class GetCharactersCall(
        val page: Int,
        val name: String?,
        val status: Status?,
        val gender: Gender?,
    )

    val resultByName: MutableMap<String?, Result<CharactersData, DataError.Remote>> =
        mutableMapOf()
    val getCharactersCalls: MutableList<GetCharactersCall> = mutableListOf()
    val requestedCharacterIds: MutableList<Int> = mutableListOf()
    val requestedCharacterIdLists: MutableList<List<Int>> = mutableListOf()

    override suspend fun getCharacters(
        page: Int,
        name: String?,
        status: Status?,
        gender: Gender?,
    ): Result<CharactersData, DataError.Remote> {
        if (getCharactersDelayMillis > 0) delay(getCharactersDelayMillis)
        getCharactersCalls += GetCharactersCall(page, name, status, gender)
        return resultByName[name] ?: defaultResult
    }

    override suspend fun getCharacter(id: Int): Result<CharacterDetails, DataError.Remote> {
        if (getCharacterDelayMillis > 0) delay(getCharacterDelayMillis)
        requestedCharacterIds += id
        return getCharacterResult
    }

    override suspend fun getCharacterById(ids: List<Int>): Result<List<CharacterDetails>, DataError.Remote> {
        if (getCharacterByIdDelayMillis > 0) delay(getCharacterByIdDelayMillis)
        requestedCharacterIdLists += ids
        return getCharacterByIdResult
    }

    fun clearCalls() {
        getCharactersCalls.clear()
    }
}
