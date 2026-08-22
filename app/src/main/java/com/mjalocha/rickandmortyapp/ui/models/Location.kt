package com.mjalocha.rickandmortyapp.ui.models

import com.mjalocha.rickandmortyapp.data.model.dto.LocationDto

data class Location(
    val name: String,
)

fun LocationDto.toLocation() =
    Location(
        name = this.name,
    )
