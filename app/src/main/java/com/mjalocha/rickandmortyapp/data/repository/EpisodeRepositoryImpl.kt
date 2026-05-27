package com.mjalocha.rickandmortyapp.data.repository

import com.mjalocha.rickandmortyapp.data.model.dto.charactersIds
import com.mjalocha.rickandmortyapp.data.network.RickAndMortyApi
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result
import com.mjalocha.rickandmortyapp.ui.models.CharacterDetails
import com.mjalocha.rickandmortyapp.ui.models.Episode
import com.mjalocha.rickandmortyapp.ui.models.toCharacter
import com.mjalocha.rickandmortyapp.ui.models.toEpisode
import org.koin.core.annotation.Singleton

@Singleton
class EpisodeRepositoryImpl(
    private val api: RickAndMortyApi,
) : EpisodeRepository {
    override suspend fun getEpisodes(
        page: Int,
        name: String?,
        episodeCode: String?,
    ): Result<List<Episode>, DataError.Remote> =
        when (
            val response =
                api.getEpisodes(
                    page = page,
                    name = name,
                    episodeCode = episodeCode,
                )
        ) {
            is Result.Success -> {
                Result.Success(
                    response.data.results.map {
                        it.toEpisode(null)
                    },
                )
            }

            is Result.Error -> Result.Error(response.error)
        }

    override suspend fun getEpisode(id: Int): Result<Episode, DataError.Remote> {
        return when (val response = api.getEpisode(id)) {
            is Result.Success -> {
                val charIds = response.data.charactersIds()
                val characters =
                    when (val chResp = api.getCharacterById(charIds)) {
                        is Result.Success -> chResp.data.map { it.toCharacter(null) }
                        is Result.Error -> null
                    }
                return Result.Success(response.data.toEpisode(characters))
            }

            is Result.Error -> Result.Error(response.error)
        }
    }

    override suspend fun getEpisodeById(ids: List<Int>): Result<List<Episode>, DataError.Remote> =
        when (val response = api.getEpisodeById(ids)) {
            is Result.Success -> {
                val allCharacterIds = response.data.flatMap { it.charactersIds() }.distinct()
                val charactersMap: Map<Int, CharacterDetails> =
                    when (val chResp = api.getCharacterById(allCharacterIds)) {
                        is Result.Success -> chResp.data.associate { it.id to it.toCharacter(null) }
                        is Result.Error -> emptyMap()
                    }

                val episodes =
                    response.data.map { episodeDto ->
                        val idsForEpisode = episodeDto.charactersIds()
                        val chars = idsForEpisode.mapNotNull { charactersMap[it] }
                        episodeDto.toEpisode(chars)
                    }
                Result.Success(episodes)
            }

            is Result.Error -> Result.Error(response.error)
        }
}
