package com.mjalocha.rickandmortyapp.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mjalocha.rickandmortyapp.R

@Composable
fun EmptyState(modifier: Modifier = Modifier) {
    LottieLoader(modifier = modifier, lottieFile = R.raw.morty, text = R.string.no_results)
}