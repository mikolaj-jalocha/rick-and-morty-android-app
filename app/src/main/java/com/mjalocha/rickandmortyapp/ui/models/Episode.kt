package com.mjalocha.rickandmortyapp.ui.models

import com.mjalocha.rickandmortyapp.data.model.dto.EpisodeDto

data class Episode(
    val id: Int,
    val name: String,
    val airDate: String,
    val episode: String,
    val characters: List<CharacterDetails>?,
    val created: String,
)

fun EpisodeDto.toEpisode(characters: List<CharacterDetails>?): Episode =
    Episode(
        id = this.id,
        name = this.name,
        airDate = this.airDate,
        episode = this.episode,
        characters = characters,
        created = this.created,
    )
