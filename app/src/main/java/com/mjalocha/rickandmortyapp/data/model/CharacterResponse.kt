package com.mjalocha.rickandmortyapp.data.model

import com.mjalocha.rickandmortyapp.data.model.dto.CharacterDto
import kotlinx.serialization.Serializable

@Serializable
data class CharacterResponse(
    val info: ResponseMetaData,
    val results: List<CharacterDto>,
)
