@file:Suppress("unused")

package com.mjalocha.rickandmortyapp.data.repository

import com.mjalocha.rickandmortyapp.data.model.dto.EpisodeDto
import com.mjalocha.rickandmortyapp.data.model.dto.episodesIds
import com.mjalocha.rickandmortyapp.data.network.RickAndMortyApi
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result
import com.mjalocha.rickandmortyapp.ui.models.CharacterDetails
import com.mjalocha.rickandmortyapp.ui.models.CharactersData
import com.mjalocha.rickandmortyapp.ui.models.toCharacter
import com.mjalocha.rickandmortyapp.ui.models.toCharacterData
import com.mjalocha.rickandmortyapp.ui.models.toEpisode
import org.koin.core.annotation.Singleton

@Singleton
class CharacterRepositoryImpl(
    private val api: RickAndMortyApi,
) : CharacterRepository {
    override suspend fun getCharacters(
        page: Int,
        name: String?,
        status: String?,
        gender: String?,
    ): Result<CharactersData, DataError.Remote> =
        when (
            val response =
                api.getCharacters(
                    page = page,
                    name = name,
                    status = status,
                    gender = gender,
                )
        ) {
            is Result.Success -> {
                Result.Success(response.data.toCharacterData())
            }

            is Result.Error -> {
                Result.Error(response.error)
            }
        }

    override suspend fun getCharacter(id: Int): Result<CharacterDetails, DataError.Remote> =
        when (val response = api.getCharacter(id)) {
            is Result.Success -> {
                val ids = response.data.episodesIds()
                val episodes =
                    when (val response = api.getEpisodeById(ids)) {
                        is Result.Success -> response.data.map { it.toEpisode(null) }
                        is Result.Error -> null
                    }
                Result.Success(response.data.toCharacter(episodes))
            }

            is Result.Error -> Result.Error(response.error)
        }

    override suspend fun getCharacterById(ids: List<Int>): Result<List<CharacterDetails>, DataError.Remote> =
        when (val response = api.getCharacterById(ids)) {
            is Result.Success -> {
                val allEpisodeIds = response.data.flatMap { it.episodesIds() }.distinct()
                val episodesMap: Map<Int, EpisodeDto> =
                    when (val response = api.getEpisodeById(allEpisodeIds)) {
                        is Result.Success -> response.data.associateBy { it.id }
                        is Result.Error -> emptyMap()
                    }

                val characters =
                    response.data.map { dto ->
                        val idsForChar = dto.episodesIds()
                        val eps = idsForChar.mapNotNull { episodesMap[it] }
                        dto.toCharacter(eps.map { it.toEpisode(null) })
                    }

                Result.Success(characters)
            }

            is Result.Error -> Result.Error(response.error)
        }
}
