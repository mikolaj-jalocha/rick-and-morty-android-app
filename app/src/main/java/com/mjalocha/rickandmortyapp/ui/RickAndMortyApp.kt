package com.mjalocha.rickandmortyapp.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.mjalocha.rickandmortyapp.ui.navigation.Navigator
import com.mjalocha.rickandmortyapp.ui.theme.RickAndMortyAppTheme
import org.koin.compose.koinInject
import org.koin.compose.navigation3.koinEntryProvider
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(KoinExperimentalAPI::class)
@Composable
@Preview
fun RickAndMortyApp() {
    RickAndMortyAppTheme {
        val entryProvider = koinEntryProvider<Any>()
        val navigator = koinInject<Navigator>()

        NavDisplay(
            backStack = navigator.backStack,
            onBack = { navigator.navigateBack() },
            entryProvider = entryProvider,
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            )
        )
    }
}