package com.mjalocha.rickandmortyapp.data.repository

import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result
import com.mjalocha.rickandmortyapp.ui.models.Episode

interface EpisodeRepository {
    suspend fun getEpisodes(
        page: Int = 1,
        name: String? = null,
        episodeCode: String? = null,
    ): Result<List<Episode>, DataError.Remote>

    suspend fun getEpisode(id: Int): Result<Episode, DataError.Remote>

    suspend fun getEpisodeById(ids: List<Int>): Result<List<Episode>, DataError.Remote>
}
