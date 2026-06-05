package com.mjalocha.rickandmortyapp.ui.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.annotation.ExperimentalCoilApi
import com.mjalocha.rickandmortyapp.R
import com.mjalocha.rickandmortyapp.data.model.dto.LocationDto
import com.mjalocha.rickandmortyapp.data.model.dto.OriginDto
import com.mjalocha.rickandmortyapp.ui.components.CharacterCard
import com.mjalocha.rickandmortyapp.ui.components.LottieLoader
import com.mjalocha.rickandmortyapp.ui.models.CharacterDetails
import com.mjalocha.rickandmortyapp.ui.theme.RickAndMortyAppTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharactersScreen(
    navigateToCharacterDetails: (characterId: Int) -> Unit,
    viewModel: CharactersViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CharactersScreen(
        characters = state.characters,
        searchPhrase = state.searchPhrase,
        isLoading = state.isLoading,
        errorMessage = state.errorMessage,
        isAtBottom = {
            viewModel.fetchCharacters()
        },
        onSearchQueryChange = {
            viewModel.onSearchQueryChange(it)
        },
        onCharacterClick = {
            navigateToCharacterDetails(it)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharactersScreen(
    characters: List<CharacterDetails>,
    searchPhrase: String,
    isLoading: Boolean,
    errorMessage: String?,
    isAtBottom: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCharacterClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    Scaffold(
        modifier = modifier,
        topBar = {
            SearchBar(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
                inputField = {
                    SearchBarDefaults.InputField(
                        query = searchPhrase,
                        onQueryChange = {
                            onSearchQueryChange(it)
                        },
                        onSearch = {
                            onSearchQueryChange(it)
                            expanded = false
                        },
                        expanded = expanded,
                        onExpandedChange = {
                            expanded = it
                        },
                        placeholder = { Text(stringResource(R.string.search)) },
                    )
                },
                expanded = false,
                onExpandedChange = {},
            ) {
            }
        },
    ) { contentPadding ->
        if (isLoading && characters.isEmpty()) {
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
                Text(text = errorMessage)
            }
        } else {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
            ) {
                Row(
                    modifier =
                        Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.characters),
                        style = MaterialTheme.typography.headlineMedium,
                    )
                    IconButton({}) {
                        Icon(painterResource(R.drawable.ic_filter), contentDescription = null)
                    }
                }

                val gridState = rememberLazyGridState()

                LaunchedEffect(searchPhrase) {
                    if (searchPhrase.isNotEmpty()) {
                        gridState.animateScrollToItem(0)
                    }
                }

                LaunchedEffect(!(gridState.canScrollForward), isAtBottom) {
                    if (!gridState.canScrollForward) {
                        isAtBottom()
                    }
                }

                LazyVerticalGrid(
                    modifier = Modifier.weight(3f),
                    columns = GridCells.Fixed(2),
                    state = gridState,
                    contentPadding = PaddingValues(all = 4.dp),
                ) {
                    items(characters, key = { it.id }) {
                        CharacterCard(
                            modifier =
                                Modifier
                                    .padding(8.dp)
                                    .animateItem(),
                            imageUrl = it.image,
                            name = it.name,
                            status = it.status,
                            onClick = {
                                onCharacterClick(it.id)
                            },
                        )
                    }
                    item(
                        span = { GridItemSpan(2) },
                    ) {
                        if (isLoading && characters.isNotEmpty()) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                LottieLoader(R.raw.loading_dots)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    device = PIXEL_9,
)
@Composable
private fun CharactersScreenPreview() {
    RickAndMortyAppTheme {
        CharactersScreen(
            searchPhrase = "",
            characters =
                listOf(
                    CharacterDetails(
                        id = 1,
                        name = "Rick Sanchez",
                        status = "Alive",
                        species = "Human",
                        gender = "Male",
                        origin = OriginDto("", ""),
                        location = LocationDto("", ""),
                        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                        episode = emptyList(),
                    ),
                    CharacterDetails(
                        id = 2,
                        name = "Morty Smith",
                        status = "Alive",
                        species = "Human",
                        gender = "Male",
                        origin = OriginDto("", ""),
                        location = LocationDto("", ""),
                        image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                        episode = emptyList(),
                    ),
                    CharacterDetails(
                        id = 7,
                        name = "Abradolf Lincler",
                        status = "Unknown",
                        species = "Human",
                        gender = "Male",
                        origin = OriginDto("", ""),
                        location = LocationDto("", ""),
                        image = "https://rickandmortyapi.com/api/character/avatar/7.jpeg",
                        episode = emptyList(),
                    ),
                    CharacterDetails(
                        id = 10,
                        name = "Alan Rails",
                        status = "Dead",
                        species = "Human",
                        gender = "Male",
                        origin = OriginDto("", ""),
                        location = LocationDto("", ""),
                        image = "https://rickandmortyapi.com/api/character/avatar/10.jpeg",
                        episode = emptyList(),
                    ),
                ),
            isLoading = false,
            errorMessage = null,
            isAtBottom = {},
            onSearchQueryChange = {},
            onCharacterClick = {},
        )
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview()
@Composable
private fun CharacterCardPreview() {
    RickAndMortyAppTheme {
        CharacterCard(
            imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
            name = "Rick Sanchez",
            status = "Alive",
            modifier = Modifier.width(200.dp),
            onClick = {},
        )
    }
}
