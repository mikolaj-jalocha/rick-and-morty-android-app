package com.mjalocha.rickandmortyapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.mjalocha.rickandmortyapp.R

// TODO: add loading indicator
@Composable
fun CharacterCard(
    imageUrl: String,
    name: String,
    status: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ElevatedCard(
        elevation = CardDefaults.elevatedCardElevation(4.dp),
        modifier = modifier.aspectRatio(0.75f),
        onClick = { onClick() },
    ) {
        Column(Modifier.Companion.fillMaxSize()) {
            AsyncImage(
                modifier =
                    Modifier.Companion
                        .weight(3f)
                        .fillMaxWidth(),
                model = imageUrl,
                contentDescription = "Characters image",
                error = painterResource(R.drawable.placeholder),
                onLoading = {
                },
                contentScale = ContentScale.Companion.Crop,
            )

            Column(
                modifier =
                    Modifier.Companion
                        .weight(1f)
                        .fillMaxSize()
                        .padding(top = 4.dp, start = 8.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = "Status: $status",
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
