package com.mjalocha.rickandmortyapp.ui.characters

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.annotation.ExperimentalCoilApi
import com.mjalocha.rickandmortyapp.R
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.ui.components.CharacterCard
import com.mjalocha.rickandmortyapp.ui.components.EmptyState
import com.mjalocha.rickandmortyapp.ui.components.ErrorState
import com.mjalocha.rickandmortyapp.ui.components.LoadingState
import com.mjalocha.rickandmortyapp.ui.models.CharacterDetails
import com.mjalocha.rickandmortyapp.ui.models.Location
import com.mjalocha.rickandmortyapp.ui.models.Origin
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
        statusFilterChips = state.statusFilterChips,
        searchPhrase = state.searchPhrase,
        isLoading = state.isLoading,
        error = state.error,
        isAtBottom = {
            viewModel.fetchCharacters()
        },
        onSearchQueryChange = {
            viewModel.onSearchQueryChange(it)
        },
        onCharacterClick = {
            navigateToCharacterDetails(it)
        },
        onStatusFilterChipClick = {
            viewModel.onStatusFilterChange(it)
        },
    )
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharactersScreen(
    characters: List<CharacterDetails>,
    statusFilterChips: List<FilterButtonState>,
    searchPhrase: String,
    isLoading: Boolean,
    error: DataError?,
    isAtBottom: () -> Unit,
    onSearchQueryChange: (String) -> Unit,
    onCharacterClick: (Int) -> Unit,
    onStatusFilterChipClick: (Int) -> Unit,
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
                LoadingState()
            }
        } else if (error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                if (error == DataError.Remote.NO_RESULTS) {
                    EmptyState(modifier = Modifier.align(Center))
                } else {
                    ErrorState(modifier = Modifier.align(Center))
                }
            }
        } else {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(contentPadding),
            ) {
                var showFilterSection by remember { mutableStateOf(false) }
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
                    IconButton({
                        showFilterSection = !showFilterSection
                    }) {
                        Icon(painterResource(R.drawable.ic_filter), contentDescription = null)
                    }
                }

                AnimatedVisibility(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    visible = showFilterSection,
                ) {
                    Column {
                        Text(text = stringResource(R.string.status))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            items(statusFilterChips) {
                                FilterChip(
                                    selected = it.isSelected,
                                    label = { Text(stringResource(it.name)) },
                                    leadingIcon = {
                                        if (it.isSelected) {
                                            Icon(
                                                painterResource(R.drawable.ic_check),
                                                contentDescription = null,
                                            )
                                        }
                                    },
                                    onClick = {
                                        onStatusFilterChipClick(it.id)
                                    },
                                )
                            }
                        }
                    }
                }

                val gridState = rememberLazyStaggeredGridState()
                LaunchedEffect(searchPhrase) {
                    if (searchPhrase.isNotEmpty()) {
                        gridState.animateScrollToItem(0)
                    }
                }
                LaunchedEffect(statusFilterChips) {
                    gridState.scrollToItem(0)
                }

                LaunchedEffect(!(gridState.canScrollForward), isAtBottom) {
                    if (!gridState.canScrollForward && characters.isNotEmpty()) {
                        isAtBottom()
                    }
                }

                LazyVerticalStaggeredGrid(
                    modifier = Modifier.weight(3f),
                    columns = StaggeredGridCells.Fixed(2),
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
                        span = StaggeredGridItemSpan.FullLine,
                    ) {
                        if (isLoading && characters.isNotEmpty()) {
                            Box(
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                contentAlignment = Center,
                            ) {
                                LoadingState()
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
            statusFilterChips = emptyList(),
            characters =
                listOf(
                    CharacterDetails(
                        id = 1,
                        name = "Rick Sanchez",
                        status = "Alive",
                        species = "Human",
                        gender = "Male",
                        origin = Origin(""),
                        location = Location(""),
                        image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                        episode = emptyList(),
                    ),
                    CharacterDetails(
                        id = 2,
                        name = "Morty Smith",
                        status = "Alive",
                        species = "Human",
                        gender = "Male",
                        origin = Origin(""),
                        location = Location(""),
                        image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                        episode = emptyList(),
                    ),
                    CharacterDetails(
                        id = 7,
                        name = "Abradolf Lincler",
                        status = "Unknown",
                        species = "Human",
                        gender = "Male",
                        origin = Origin(""),
                        location = Location(""),
                        image = "https://rickandmortyapi.com/api/character/avatar/7.jpeg",
                        episode = emptyList(),
                    ),
                    CharacterDetails(
                        id = 10,
                        name = "Alan Rails",
                        status = "Dead",
                        species = "Human",
                        gender = "Male",
                        origin = Origin(""),
                        location = Location(""),
                        image = "https://rickandmortyapi.com/api/character/avatar/10.jpeg",
                        episode = emptyList(),
                    ),
                ),
            isLoading = false,
            error = null,
            isAtBottom = {},
            onSearchQueryChange = {},
            onCharacterClick = {},
            onStatusFilterChipClick = {},
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
