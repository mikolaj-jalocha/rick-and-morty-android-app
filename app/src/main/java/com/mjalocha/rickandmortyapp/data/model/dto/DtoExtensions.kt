package com.mjalocha.rickandmortyapp.data.model.dto

fun CharacterDto.episodesIds(): List<Int> =
    this.episode.mapNotNull { episodeUrl ->
        episodeUrl.substringAfterLast("episode/").toIntOrNull()
    }

fun EpisodeDto.charactersIds(): List<Int> =
    this.characters.mapNotNull { charUrl ->
        charUrl.substringAfterLast("character/").toIntOrNull()
    }

