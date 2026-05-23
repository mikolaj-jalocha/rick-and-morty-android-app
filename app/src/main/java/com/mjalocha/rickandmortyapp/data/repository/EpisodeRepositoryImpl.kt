package com.mjalocha.rickandmortyapp.data.repository

import com.mjalocha.rickandmortyapp.data.model.EpisodeResponse
import com.mjalocha.rickandmortyapp.data.model.dto.EpisodeDto
import com.mjalocha.rickandmortyapp.data.network.RickAndMortyApi
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result
import org.koin.core.annotation.Singleton

@Singleton
class EpisodeRepositoryImpl(
    private val apiClient: RickAndMortyApi
) : EpisodeRepository {
    override suspend fun getEpisodes(
        page: Int,
        name: String?,
        episodeCode: String?
    ): Result<EpisodeResponse, DataError.Remote> {
        return apiClient.getEpisodes(
            page = page,
            name = name,
            episodeCode = episodeCode
        )
    }


    override suspend fun getEpisodeById(ids: List<Int>): Result<List<EpisodeDto>, DataError.Remote> {
        return apiClient.getEpisodeById(
            ids = ids
        )
    }
}