package com.mjalocha.rickandmortyapp.ui.episode_details_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mjalocha.rickandmortyapp.R
import com.mjalocha.rickandmortyapp.ui.components.CharacterCard
import com.mjalocha.rickandmortyapp.ui.components.LottieLoader
import com.mjalocha.rickandmortyapp.ui.models.Episode
import com.mjalocha.rickandmortyapp.ui.theme.RickAndMortyAppTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun EpisodeDetailsScreen(
    episodeId: Int,
    onNavigateBack: () -> Unit,
    navigateToCharacterDetails: (Int) -> Unit,
    viewModel: EpisodeDetailsViewModel = koinViewModel { parametersOf(episodeId) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EpisodeDetailsScreen(
        episode = state.episode,
        isLoading = state.isLoading,
        onNavigateBack = onNavigateBack,
        onCharacterClick = navigateToCharacterDetails
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EpisodeDetailsScreen(
    episode: Episode?,
    isLoading: Boolean,
    onNavigateBack: () -> Unit,
    onCharacterClick: (Int) -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(episode?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painterResource(R.drawable.ic_back),
                            contentDescription = "Navigate back"
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                LottieLoader(R.raw.loading_dots)
            }
        } else if (episode != null) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.padding(contentPadding),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val details = listOf(
                    "Episode name" to episode.name,
                    "Air date" to episode.airDate,
                    "Episode" to episode.episode,
                    "Created" to episode.created
                )

                items(details.size, span = { GridItemSpan(maxLineSpan) }) { index ->
                    val (title, value) = details[index]
                    EpisodeDetailsListItem(
                        title = title,
                        value = value
                    )
                }

                val characters = episode.characters.orEmpty()
                items(characters, key = { it.id }) { character ->
                    CharacterCard(
                        modifier = Modifier
                            .padding(4.dp)
                            .animateItem(),
                        imageUrl = character.image,
                        name = character.name,
                        status = character.status,
                        onClick = { onCharacterClick(character.id) }
                    )
                }

            }
        }
    }
}


@Composable
fun EpisodeDetailsListItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    OutlinedCard(
        modifier = modifier,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EpisodeDetailsScreenPreview() {
    RickAndMortyAppTheme {
        EpisodeDetailsScreen(
            episode = Episode(
                id = 1,
                name = "Pilot",
                airDate = "December 2, 2013",
                episode = "S01E01",
                characters = emptyList(),
                created = "2017-11-10T12:56:33.798Z"
            ),
            isLoading = false,
            onNavigateBack = {},
            onCharacterClick = {}
        )
    }
}

@Preview(
    showBackground = true,
    uiMode = UI_MODE_NIGHT_YES
)
@Composable
fun EpisodeDetailsScreenPreviewDark() {
    RickAndMortyAppTheme {
        EpisodeDetailsScreen(
            episode = Episode(
                id = 1,
                name = "Pilot",
                airDate = "December 2, 2013",
                episode = "S01E01",
                characters = emptyList(),
                created = "2017-11-10T12:56:33.798Z"
            ),
            isLoading = false,
            onNavigateBack = {},
            onCharacterClick = {}
        )
    }
}