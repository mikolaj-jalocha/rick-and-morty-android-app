package com.mjalocha.rickandmortyapp.ui.models

import com.mjalocha.rickandmortyapp.data.model.dto.OriginDto

data class Origin(val name: String)

fun OriginDto.toOrigin() = Origin(
    name = this.name
)
