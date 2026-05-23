package com.mjalocha.rickandmortyapp.data.repository

import androidx.compose.ui.util.fastMapNotNull
import com.mjalocha.rickandmortyapp.data.model.Gender
import com.mjalocha.rickandmortyapp.data.model.Status
import com.mjalocha.rickandmortyapp.data.model.dto.CharacterDto
import com.mjalocha.rickandmortyapp.data.model.dto.EpisodeDto
import com.mjalocha.rickandmortyapp.data.network.RickAndMortyApi
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result
import com.mjalocha.rickandmortyapp.data.utils.map
import com.mjalocha.rickandmortyapp.ui.models.CharacterDetails
import org.koin.core.annotation.Singleton

@Singleton
class CharacterRepositoryImpl(
    private val apiClient: RickAndMortyApi,
    private val episodeRepository: EpisodeRepository
) : CharacterRepository {
    override suspend fun getCharacters(
        page: Int,
        name: String?,
        status: Status?,
        gender: Gender?
    ): Result<List<CharacterDetails>, DataError.Remote> {

        return when (val response = apiClient.getCharacters(
            page = page,
            name = name,
            status = status,
            gender = gender
        )) {
            is Result.Success -> {
                Result.Success(response.data.results.map {
                    it.toCharacter(null)
                })
            }

            is Result.Error -> {
                Result.Error(response.error)
            }
        }
    }

    override suspend fun getCharacter(id: Int): Result<CharacterDetails, DataError.Remote> {
        return apiClient.getCharacter(id)
            .map {
                val episodes = fetchEpisodes(it.episodesIds())
                it.toCharacter(episodes)
            }
    }

    private suspend fun fetchEpisodes(ids: List<Int>): List<EpisodeDto>? {
        return when (val episodeResponse = episodeRepository.getEpisodeById(ids)) {
            is Result.Success -> episodeResponse.data
            is Result.Error -> null
        }
    }
}

private fun CharacterDto.toCharacter(episodes: List<EpisodeDto>?): CharacterDetails {
    return CharacterDetails(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        origin = this.origin,
        location = this.location,
        image = this.image,
        episode = episodes
    )
}

private fun CharacterDto.episodesIds(): List<Int> {
    return this.episode.fastMapNotNull { episodeUrl ->
        episodeUrl.substringAfter("episode/").toIntOrNull()
    }
}
