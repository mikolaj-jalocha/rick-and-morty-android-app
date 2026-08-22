package com.mjalocha.rickandmortyapp.ui.components

import androidx.annotation.RawRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mjalocha.rickandmortyapp.R

@Composable
fun ErrorState(
    modifier: Modifier = Modifier,
    @RawRes
    lottieFile: Int = R.raw.rick,
    @StringRes
    text: Int = R.string.something_went_wrong,
) {
    LottieLoader(
        modifier = modifier,
        lottieFile = lottieFile,
        text = text,
    )
}
