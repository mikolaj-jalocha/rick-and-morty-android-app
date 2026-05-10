package com.mjalocha.rickandmortyapp.ui.characters_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import com.mjalocha.rickandmortyapp.R
import com.mjalocha.rickandmortyapp.data.model.dto.CharacterDto
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharactersScreen(
    viewModel: CharactersViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CharactersScreen(
        characters = state.characters,
        isLoading = state.isLoading,
        errorMessage = state.errorMessage
    )
}

@Composable
private fun CharactersScreen(
    characters: List<CharacterDto>,
    isLoading: Boolean,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2)
    ) {
        items(characters) {
            CharacterCard(
                imageUrl = it.url,
                name = it.name,
                status = it.status
            )
        }
    }
}

// TODO: add loading indicator
@Composable
fun CharacterCard(
    imageUrl: String,
    name: String,
    status: String,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        modifier = modifier.aspectRatio(0.75f)
    ) {
        Column(Modifier.fillMaxSize()) {
            AsyncImage(
                modifier = Modifier
                    .weight(3f)
                    .fillMaxWidth(),
                model = imageUrl,
                contentDescription = "Characters image",
                error = painterResource(R.drawable.placeholder),
                onLoading = {

                },
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(top = 4.dp, start = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "Status: $status",
                    style = MaterialTheme.typography.bodyMedium
                )
            }

        }
    }
}


@OptIn(ExperimentalCoilApi::class)
@Preview()
@Composable
fun CharacterCardPreview() {
    CharacterCard(
        imageUrl = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
        name = "Rick Sanchez",
        status = "Alive",
        modifier = Modifier.width(200.dp)
    )
}