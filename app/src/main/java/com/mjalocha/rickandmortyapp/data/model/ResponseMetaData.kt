package com.mjalocha.rickandmortyapp.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ResponseMetaData(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?,
)
