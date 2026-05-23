package com.mjalocha.rickandmortyapp.data.model

import com.mjalocha.rickandmortyapp.data.model.dto.EpisodeDto
import kotlinx.serialization.Serializable

@Serializable
data class EpisodeResponse(
    val info: ResponseMetaData,
    val results: List<EpisodeDto>
)
