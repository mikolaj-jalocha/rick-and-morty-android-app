package com.mjalocha.rickandmortyapp.ui.characters_screen

import androidx.annotation.RawRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_9
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.mjalocha.rickandmortyapp.R
import com.mjalocha.rickandmortyapp.data.model.dto.CharacterDto
import com.mjalocha.rickandmortyapp.data.model.dto.LocationDto
import com.mjalocha.rickandmortyapp.data.model.dto.OriginDto
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
    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            LottieLoader(R.raw.loading_dots)
        }
    } else {
        LazyVerticalGrid(
            modifier = modifier,
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 32.dp)
        ) {
            items(characters) {
                CharacterCard(
                    modifier = Modifier.padding(8.dp),
                    imageUrl = it.image,
                    name = it.name,
                    status = it.status
                )
            }
        }
    }
}

@Composable
fun LottieLoader(
    @RawRes
    lottieFile: Int,
    modifier: Modifier = Modifier
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(lottieFile))
    LottieAnimation(
        modifier = modifier,
        composition = composition,
        iterations = LottieConstants.IterateForever,
    )
}

@Preview(
    showSystemUi = true,
    showBackground = true,
    device = PIXEL_9
)
@Composable
fun CharactersScreenPreview() {
    CharactersScreen(
        characters = listOf(
            CharacterDto(
                id = 1,
                name = "Rick Sanchez",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = OriginDto("", ""),
                location = LocationDto("", ""),
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                episode = emptyList(),
                url = "",
                created = "2017-11-04T18:48:46.250Z"
            ),
            CharacterDto(
                id = 2,
                name = "Morty Smith",
                status = "Alive",
                species = "Human",
                type = "",
                gender = "Male",
                origin = OriginDto("", ""),
                location = LocationDto("", ""),
                image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                episode = emptyList(),
                url = "",
                created = "2017-11-04T18:50:21.651Z"
            ),
            CharacterDto(
                id = 7,
                name = "Abradolf Lincler",
                status = "Unknown",
                species = "Human",
                type = "Genetic experiment",
                gender = "Male",
                origin = OriginDto("", ""),
                location = LocationDto("", ""),
                image = "https://rickandmortyapi.com/api/character/avatar/7.jpeg",
                episode = emptyList(),
                url = "",
                created = "2017-11-04T19:59:20.523Z"
            ),
            CharacterDto(
                id = 10,
                name = "Alan Rails",
                status = "Dead",
                species = "Human",
                type = "Superhuman",
                gender = "Male",
                origin = OriginDto("", ""),
                location = LocationDto("", ""),
                image = "https://rickandmortyapi.com/api/character/avatar/10.jpeg",
                episode = emptyList(),
                url = "",
                created = "2017-11-04T20:19:09.017Z"
            )
        ),
        isLoading = false,
        errorMessage = null,
    )
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
        elevation = CardDefaults.elevatedCardElevation(4.dp),
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