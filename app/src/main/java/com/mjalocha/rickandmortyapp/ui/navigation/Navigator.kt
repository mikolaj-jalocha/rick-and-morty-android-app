package com.mjalocha.rickandmortyapp.ui.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import org.koin.core.annotation.Singleton

@Singleton
class Navigator(startDestination: Any = Route.CharacterList) {
    val backStack: SnapshotStateList<Any> = mutableStateListOf(startDestination)

    fun navigateTo(destination: Any) {
        backStack.add(destination)
    }

    fun navigateBack() {
        backStack.removeLastOrNull()
    }
}