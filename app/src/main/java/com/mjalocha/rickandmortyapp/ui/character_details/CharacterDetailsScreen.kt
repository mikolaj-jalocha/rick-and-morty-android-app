package com.mjalocha.rickandmortyapp.ui.character_details

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
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
import com.mjalocha.rickandmortyapp.ui.theme.RickAndMortyAppTheme
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CharacterDetailsScreen(
    characterId: Int,
    onNavigateBack: () -> Unit,
    viewModel: CharacterDetailsViewModel = koinViewModel { parametersOf(characterId) }
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    CharacterDetailsScreen(
        character = state.characterDetails,
        isLoading = state.isLoading,
        onNavigateBack = onNavigateBack
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterDetailsScreen(
    character: CharacterDetails?,
    isLoading: Boolean,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(character?.name ?: "") },
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
        } else if (character != null) {
            LazyColumn(
                modifier = modifier.padding(contentPadding)
            ) {
                item {
                    AsyncImage(
                        model = character.image,
                        contentDescription = "Image of the character",
                        error = painterResource(R.drawable.placeholder),
                        modifier = Modifier
                            .fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = character.name,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentWidth(Alignment.CenterHorizontally)

                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        //TODO: extract to domain model
                        val contentColor =
                            if (character.status == "Alive") Color(0xFF97CE4C) else Color(
                                0xFFFF0000
                            )

                        Icon(
                            painterResource(R.drawable.ic_circle),
                            contentDescription = null,
                            tint = contentColor,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(16.dp)
                        )

                        Text(
                            text = character.status,
                            style = MaterialTheme.typography.labelLarge,
                            color = contentColor
                        )
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        CharacterCardRowItem(
                            title = "Species",
                            value = character.species,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 16.dp,
                                top = 8.dp
                            )
                        )
                        CharacterCardRowItem(
                            title = "Gender",
                            value = character.gender,
                            modifier = Modifier.padding(
                                start = 16.dp,
                                end = 16.dp,
                                bottom = 8.dp
                            )
                        )
                    }

                    Text(
                        text = "ORIGIN",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    CharacterDetailsListItem(
                        title = character.origin.name,
                        leadingIcon = R.drawable.ic_globe,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "LAST KNOWN LOCATION",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = 1.sp,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    CharacterDetailsListItem(
                        title = character.location.name,
                        leadingIcon = R.drawable.ic_globe,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun CharacterDetailsListItem(
    title: String,
    @DrawableRes
    leadingIcon: Int,
    modifier: Modifier = Modifier,
) {
    ListItem(
        modifier = modifier
            .clip(CardDefaults.shape),
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
            headlineColor = MaterialTheme.colorScheme.tertiary,
            leadingIconColor = MaterialTheme.colorScheme.primary,
            trailingIconColor = MaterialTheme.colorScheme.onSurface
        ),
        leadingContent = {
            Icon(painterResource(leadingIcon), contentDescription = null)
        },
        headlineContent = {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        },
        trailingContent = {
            Icon(
                painterResource(R.drawable.ic_next),
                contentDescription = "See details of the character's origin"
            )
        }
    )
}

@Composable
private fun CharacterCardRowItem(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
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
    val mockCharacter = CharacterDetails(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        gender = "Male",
        origin = OriginDto(name = "Earth", url = ""),
        location = LocationDto(name = "Earth", url = ""),
        image = "",
        episode = null
    )
    RickAndMortyAppTheme {
        CharacterDetailsScreen(
            character = mockCharacter,
            isLoading = false,
            onNavigateBack = {}
        )
    }
}

@Preview(showBackground = true, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun CharacterDetailsScreenPreviewDarkMode() {
    val mockCharacter = CharacterDetails(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        gender = "Male",
        origin = OriginDto(name = "Earth", url = ""),
        location = LocationDto(name = "Earth", url = ""),
        image = "",
        episode = null
    )
    RickAndMortyAppTheme {
        CharacterDetailsScreen(
            character = mockCharacter,
            isLoading = false,
            onNavigateBack = {}
        )
    }
}