package com.mjalocha.rickandmortyapp.data.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(
    val name: String,
    val url: String,
)
