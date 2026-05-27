package com.mjalocha.rickandmortyapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class OriginDto(
    val name: String,
    val url: String,
)
