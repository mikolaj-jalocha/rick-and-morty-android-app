package com.mjalocha.rickandmortyapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
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
        modifier = modifier,
        onClick = { onClick() },
    ) {
        Column(Modifier.fillMaxWidth()) {
            AsyncImage(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f),
                model = imageUrl,
                contentDescription = stringResource(R.string.image_of_the_character),
                error = painterResource(R.drawable.placeholder),
                onLoading = {
                },
                contentScale = ContentScale.Crop,
            )

            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .heightIn(min = 72.dp)
                        .wrapContentHeight()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = name,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = stringResource(R.string.status) + ":" + status,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
