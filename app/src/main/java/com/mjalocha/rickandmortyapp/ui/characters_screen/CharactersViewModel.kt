package com.mjalocha.rickandmortyapp.ui.characters_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjalocha.rickandmortyapp.data.model.dto.CharacterDto
import com.mjalocha.rickandmortyapp.data.repository.CharacterRepository
import com.mjalocha.rickandmortyapp.data.utils.onError
import com.mjalocha.rickandmortyapp.data.utils.onSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharactersViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CharactersScreenState())
    val state: StateFlow<CharactersScreenState> = _state

    private var charactersFetchJob: Job? = null

    init {
        fetchCharacters()
    }

    fun fetchCharacters() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }

        charactersFetchJob?.cancel()
        charactersFetchJob = viewModelScope.launch(Dispatchers.IO) {
            repository.getCharacters().onSuccess { data ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        characters = data.results
                    )
                }
            }.onError { error ->
                _state.update {
                    it.copy(
                        errorMessage = error.name,
                        isLoading = false
                    )
                }
            }
        }
    }
}

// TODO: replace DTO with domain model
data class CharactersScreenState(
    val characters: List<CharacterDto> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)