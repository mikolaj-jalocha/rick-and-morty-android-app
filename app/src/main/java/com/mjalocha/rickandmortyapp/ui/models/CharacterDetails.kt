package com.mjalocha.rickandmortyapp.ui.models

import com.mjalocha.rickandmortyapp.data.model.dto.CharacterDto
import com.mjalocha.rickandmortyapp.data.model.dto.LocationDto
import com.mjalocha.rickandmortyapp.data.model.dto.OriginDto

data class CharacterDetails(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val origin: OriginDto,
    val location: LocationDto,
    val image: String,
    val episode: List<Episode>?,
)

fun CharacterDto.toCharacter(episodes: List<Episode>?): CharacterDetails =
    CharacterDetails(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        origin = this.origin,
        location = this.location,
        image = this.image,
        episode = episodes,
    )
