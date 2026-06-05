package com.mjalocha.rickandmortyapp.ui.components

import androidx.annotation.RawRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mjalocha.rickandmortyapp.R

@Composable
fun LoadingState(
    modifier: Modifier = Modifier,
    @RawRes
    lottieFile: Int = R.raw.loading,
) {
    LottieLoader(
        modifier = modifier, lottieFile = lottieFile, text = R.string.loading
    )
}