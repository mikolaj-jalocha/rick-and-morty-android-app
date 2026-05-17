package com.mjalocha.rickandmortyapp.ui.characters_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjalocha.rickandmortyapp.data.model.dto.CharacterDto
import com.mjalocha.rickandmortyapp.data.repository.CharacterRepository
import com.mjalocha.rickandmortyapp.data.utils.onError
import com.mjalocha.rickandmortyapp.data.utils.onSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class CharactersViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CharactersScreenState())
    val state = _state.asStateFlow()

    private var charactersFetchJob: Job? = null

    init {
        fetchCharacters()
    }

    fun onSearchQueryChange(query: String) {
        _state.update {
            it.copy(
                searchPhrase = query
            )
        }
        charactersFetchJob?.cancel()
        charactersFetchJob = viewModelScope.launch {
            delay(timeMillis = 500L)
            repository.getCharacters(
                name = _state.value.searchPhrase
            )
                .onSuccess { data ->
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
                            isLoading = false,
                            characters = emptyList()
                        )
                    }
                }
        }
    }

    fun fetchCharacters() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }

        charactersFetchJob?.cancel()
        charactersFetchJob = viewModelScope.launch {
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
                        isLoading = false,
                        characters = emptyList()
                    )
                }
            }
        }
    }
}

// TODO: replace DTO with domain model
data class CharactersScreenState(
    val characters: List<CharacterDto> = emptyList(),
    val searchPhrase: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)