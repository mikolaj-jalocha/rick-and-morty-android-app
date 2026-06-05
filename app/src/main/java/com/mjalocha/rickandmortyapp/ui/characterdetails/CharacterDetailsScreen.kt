package com.mjalocha.rickandmortyapp.ui.characterdetails

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.AndroidUiModes.UI_MODE_NIGHT_YES
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.mjalocha.rickandmortyapp.R
import com.mjalocha.rickandmortyapp.data.model.dto.LocationDto
import com.mjalocha.rickandmortyapp.data.model.dto.OriginDto
import com.mjalocha.rickandmortyapp.ui.components.LottieLoader
import com.mjalocha.rickandmortyapp.ui.models.CharacterDetails
import com.mjalocha.rickandmortyapp.ui.models.Episode
import com.mjalocha.rickandmortyapp.ui.theme.RickAndMortyAppTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    onNavigateBack: () -> Unit,
    onEpisodeClick: (Int) -> Unit,
    viewModel: CharacterDetailsViewModel = koinViewModel { parametersOf(characterId) },
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CharacterDetailsScreen(
        character = state.characterDetails,
        isLoading = state.isLoading,
        errorMessage = state.errorMessage,
        onNavigateBack = onNavigateBack,
        onEpisodeClick = onEpisodeClick,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterDetailsScreen(
    character: CharacterDetails?,
    isLoading: Boolean,
    errorMessage: String?,
    onNavigateBack: () -> Unit,
    onEpisodeClick: (Int) -> Unit = {},
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(character?.name ?: "") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            painterResource(R.drawable.ic_back),
                            contentDescription = stringResource(R.string.navigate_back),
                        )
                    }
                },
            )
        },
    ) { contentPadding ->
        if (isLoading) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
            ) {
                LottieLoader(R.raw.loading_dots)
            }
        } else if (errorMessage != null) {
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
            ) {
                Text(text = stringResource(R.string.something_went_wrong))
            }
        } else if (character != null) {
            LazyColumn(
                contentPadding = contentPadding,
            ) {
                item {
                    AsyncImage(
                        model = character.image,
                        contentDescription = stringResource(R.string.image_of_the_character),
                        error = painterResource(R.drawable.placeholder),
                        modifier =
                            Modifier
                                .fillMaxWidth(),
                        contentScale = ContentScale.Crop,
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .wrapContentWidth(Alignment.CenterHorizontally),
                    )

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                    ) {
                        // TODO: extract to domain model
                        val contentColor =
                            if (character.status == "Alive") {
                                Color(0xFF97CE4C)
                            } else {
                                Color(
                                    0xFFFF0000,
                                )
                            }

                        Icon(
                            painterResource(R.drawable.ic_circle),
                            contentDescription = null,
                            tint = contentColor,
                            modifier =
                                Modifier
                                    .padding(horizontal = 4.dp)
                                    .size(16.dp),
                        )

                        Text(
                            text = character.status,
                            style = MaterialTheme.typography.labelLarge,
                            color = contentColor,
                        )
                    }

                    Card(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                    ) {
                        CharacterCardRowItem(
                            title = stringResource(R.string.species),
                            value = character.species,
                            modifier =
                                Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 16.dp,
                                    top = 8.dp,
                                ),
                        )
                        CharacterCardRowItem(
                            title = stringResource(R.string.gender),
                            value = character.gender,
                            modifier =
                                Modifier.padding(
                                    start = 16.dp,
                                    end = 16.dp,
                                    bottom = 8.dp,
                                ),
                        )
                    }

                    Text(
                        text = stringResource(R.string.origin).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )

                    CharacterDetailsListItem(
                        title = character.origin.name,
                        leadingIcon = R.drawable.ic_globe,
                        modifier = Modifier.padding(16.dp),
                    )
                    Text(
                        text = stringResource(R.string.last_known_location).uppercase(),
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 16.dp),
                    )
                    CharacterDetailsListItem(
                        title = character.location.name,
                        leadingIcon = R.drawable.ic_globe,
                        modifier = Modifier.padding(16.dp),
                    )
                }
                item {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        items(
                            items = character.episode ?: emptyList(),
                            key = { it.id },
                        ) { episode ->
                            EpisodeItem(
                                episode = episode,
                                modifier = Modifier,
                                onClick = { onEpisodeClick(episode.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeItem(
    episode: Episode,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    val colors =
        listOf(
            Color(0xFFFAFD7C),
            Color(0xFF82491E),
            Color(0xFF24325F),
            Color(0xFFB7E4F9),
            Color(0xFFFB6467),
            Color(0xFF526E2D),
            Color(0xFFE762D7),
            Color(0xFFE89242),
            Color(0xFFFAE48B),
            Color(0xFFA6EEE6),
            Color(0xFF917C5D),
            Color(0xFF69C8EC),
        )

    Card(
        modifier = modifier.clickable(onClick = onClick),
        border = BorderStroke(width = 2.dp, color = colors.random()),
    ) {
        Column(
            modifier = Modifier.padding(top = 8.dp, bottom = 8.dp, start = 8.dp, end = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = stringResource(R.string.episode) + episode.episode,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = episode.name,
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = stringResource(R.string.aired_on) + episode.airDate,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Preview
@Composable
private fun EpisodeItemPreview() {
    RickAndMortyAppTheme {
        EpisodeItem(
            Episode(
                id = 1,
                name = "Pilot",
                airDate = "December 2, 2013",
                episode = "S01E01",
                characters = emptyList(),
                created = "",
            ),
        )
    }
}

@Composable
fun CharacterDetailsListItem(
    title: String,
    @DrawableRes
    leadingIcon: Int,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier =
            modifier
                .clip(CardDefaults.shape),
        colors =
            ListItemDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                headlineColor = MaterialTheme.colorScheme.tertiary,
                leadingIconColor = MaterialTheme.colorScheme.primary,
                trailingIconColor = MaterialTheme.colorScheme.onSurface,
            ),
        leadingContent = {
            Icon(painterResource(leadingIcon), contentDescription = null)
        },
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
        },
        trailingContent = {
//            Icon(
//                painterResource(R.drawable.ic_next),
//                contentDescription = "See details of the character's origin",
//            )
        },
    )
}

@Composable
private fun CharacterCardRowItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier =
            modifier
                .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun CharacterDetailsScreenPreview() {
    val mockCharacter =
        CharacterDetails(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            gender = "Male",
            origin = OriginDto(name = "Earth", url = ""),
            location = LocationDto(name = "Earth", url = ""),
            image = "",
            episode =
                listOf(
                    Episode(
                        id = 1,
                        name = "Pilot",
                        airDate = "December 2, 2013",
                        episode = "S01E01",
                        characters = emptyList(),
                        created = "",
                    ),
                    Episode(
                        id = 2,
                        name = "Pilot",
                        airDate = "December 2, 2013",
                        episode = "S01E01",
                        characters = emptyList(),
                        created = "",
                    ),
                    Episode(
                        id = 3,
                        name = "Pilot",
                        airDate = "December 2, 2013",
                        episode = "S01E01",
                        characters = emptyList(),
                        created = "",
                    ),
                ),
        )
    RickAndMortyAppTheme {
        CharacterDetailsScreen(
            character = mockCharacter,
            isLoading = false,
            errorMessage = null,
            onNavigateBack = {},
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailsScreenPreviewDarkMode() {
    val mockCharacter =
        CharacterDetails(
            id = 1,
            name = "Rick Sanchez",
            status = "Alive",
            species = "Human",
            gender = "Male",
            origin = OriginDto(name = "Earth", url = ""),
            location = LocationDto(name = "Earth", url = ""),
            image = "",
            episode =
                listOf(
                    Episode(
                        id = 1,
                        name = "Pilot",
                        airDate = "December 2, 2013",
                        episode = "S01E01",
                        characters = emptyList(),
                        created = "",
                    ),
                    Episode(
                        id = 2,
                        name = "Pilot",
                        airDate = "December 2, 2013",
                        episode = "S01E01",
                        characters = emptyList(),
                        created = "",
                    ),
                    Episode(
                        id = 3,
                        name = "Pilot",
                        airDate = "December 2, 2013",
                        episode = "S01E01",
                        characters = emptyList(),
                        created = "",
                    ),
                ),
        )
    RickAndMortyAppTheme {
        CharacterDetailsScreen(
            character = mockCharacter,
            isLoading = false,
            errorMessage = null,
            onNavigateBack = {},
        )
    }
}
