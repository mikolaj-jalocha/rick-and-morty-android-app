package com.mjalocha.rickandmortyapp.di

import com.mjalocha.rickandmortyapp.ui.character_details.CharacterDetailsScreen
import com.mjalocha.rickandmortyapp.ui.characters_screen.CharactersScreen
import com.mjalocha.rickandmortyapp.ui.episode_details_screen.EpisodeDetailsScreen
import com.mjalocha.rickandmortyapp.ui.navigation.Navigator
import com.mjalocha.rickandmortyapp.ui.navigation.Route
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.dsl.navigation3.navigation

@OptIn(KoinExperimentalAPI::class)
val navigationModule = module {

    navigation<Route.CharacterList> { route ->
        CharactersScreen(
            navigateToCharacterDetails = { characterId ->
                get<Navigator>().navigateTo(destination = Route.CharacterDetail(characterId = characterId))
            }
        )
    }

    navigation<Route.CharacterDetail> { route ->
        CharacterDetailsScreen(
            characterId = route.characterId,
            onNavigateBack = {
                get<Navigator>().navigateBack()
            },
            onEpisodeClick = { episodeId ->
                get<Navigator>().navigateTo(destination = Route.EpisodeDetail(episodeId = episodeId))
            }
        )
    }

    navigation<Route.EpisodeDetail> { route ->
        EpisodeDetailsScreen(
            episodeId = route.episodeId,
            onNavigateBack = {
                get<Navigator>().navigateBack()
            },
            navigateToCharacterDetails = { characterId ->
                get<Navigator>().navigateTo(destination = Route.CharacterDetail(characterId = characterId))
            }
        )
    }
}