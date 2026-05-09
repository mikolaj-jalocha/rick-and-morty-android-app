package com.mjalocha.rickandmortyapp.data.model

data class ResponseMetaData(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String?
)