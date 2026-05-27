package com.mjalocha.rickandmortyapp.ui.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Route {
    @Serializable
    data object CharacterList

    @Serializable
    data class CharacterDetail(
        val characterId: Int,
    )

    @Serializable
    data class EpisodeDetail(
        val episodeId: Int,
    )
}
