package com.mjalocha.rickandmortyapp.data.repository

import com.mjalocha.rickandmortyapp.data.model.EpisodeResponse
import com.mjalocha.rickandmortyapp.data.model.dto.EpisodeDto
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result

interface EpisodeRepository {
    suspend fun getEpisodes(
        page: Int = 1,
        name: String? = null,
        episodeCode: String? = null
    ): Result<EpisodeResponse, DataError.Remote>

    suspend fun getEpisodeById(
        ids: List<Int>
    ): Result<List<EpisodeDto>, DataError.Remote>
}
