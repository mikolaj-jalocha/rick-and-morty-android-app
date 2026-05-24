package com.mjalocha.rickandmortyapp.data.network

import com.mjalocha.rickandmortyapp.data.model.CharacterResponse
import com.mjalocha.rickandmortyapp.data.model.EpisodeResponse
import com.mjalocha.rickandmortyapp.data.model.Gender
import com.mjalocha.rickandmortyapp.data.model.Status
import com.mjalocha.rickandmortyapp.data.model.dto.CharacterDto
import com.mjalocha.rickandmortyapp.data.model.dto.EpisodeDto
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result

interface RickAndMortyApi {
    suspend fun getCharacters(
        page: Int = 1,
        name: String? = null,
        status: Status? = null,
        gender: Gender? = null
    ): Result<CharacterResponse, DataError.Remote>

    suspend fun getCharacter(
        id: Int
    ): Result<CharacterDto, DataError.Remote>

    suspend fun getCharacterById(
        ids: List<Int>
    ): Result<List<CharacterDto>, DataError.Remote>

    suspend fun getEpisode(id: Int): Result<EpisodeDto, DataError.Remote>
    suspend fun getEpisodes(
        page: Int = 1,
        name: String? = null,
        episodeCode: String? = null
    ): Result<EpisodeResponse, DataError.Remote>

    suspend fun getEpisodeById(
        ids: List<Int>
    ): Result<List<EpisodeDto>, DataError.Remote>
}