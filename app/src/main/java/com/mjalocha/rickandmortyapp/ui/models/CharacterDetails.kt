package com.mjalocha.rickandmortyapp.ui.models

import com.mjalocha.rickandmortyapp.data.model.CharacterResponse
import com.mjalocha.rickandmortyapp.data.model.dto.CharacterDto

data class CharactersData(
    val nextPage: Int?,
    val results: List<CharacterDetails>,
)

data class CharacterDetails(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val origin: Origin,
    val location: Location,
    val image: String,
    val episode: List<Episode>?,
)

fun CharacterResponse.toCharacterData(): CharactersData {
    val nextPage = this.info.next.extractPageNumber()
    val characterDetails = this.results.map { it.toCharacter(null) }

    return CharactersData(
        nextPage = nextPage,
        results = characterDetails,
    )
}

fun String?.extractPageNumber(): Int? {
    if (this != null) {
        return this
            .substringAfter("page=")
            .substringBefore("&")
            .toIntOrNull()
    }
    return null
}

fun CharacterDto.toCharacter(episodes: List<Episode>?): CharacterDetails =
    CharacterDetails(
        id = this.id,
        name = this.name,
        status = this.status,
        species = this.species,
        gender = this.gender,
        origin = this.origin.toOrigin(),
        location = this.location.toLocation(),
        image = this.image,
        episode = episodes,
    )
