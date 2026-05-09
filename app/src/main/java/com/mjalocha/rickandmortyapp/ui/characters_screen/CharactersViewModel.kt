package com.mjalocha.rickandmortyapp.ui.characters_screen

import androidx.lifecycle.ViewModel
import com.mjalocha.rickandmortyapp.data.repository.CharacterRepository

class CharactersViewModel(
    private val characterRepository: CharacterRepository
) : ViewModel() {

}