package com.mjalocha.rickandmortyapp.ui.character_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjalocha.rickandmortyapp.data.model.dto.CharacterDto
import com.mjalocha.rickandmortyapp.data.repository.CharacterRepository
import com.mjalocha.rickandmortyapp.data.utils.onError
import com.mjalocha.rickandmortyapp.data.utils.onSuccess
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class CharacterDetailsViewModel(
    private val repository: CharacterRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CharacterDetailsScreenState())
    val state = _state.asStateFlow()

    private var characterFetchJob: Job? = null

    init {
        fetchCharacter()
    }

    private fun fetchCharacter() {
        _state.update {
            it.copy(
                isLoading = true
            )
        }
        characterFetchJob?.cancel()
        characterFetchJob = viewModelScope.launch {
            repository.getCharacter(id = 1).onSuccess { data ->
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        characterDetails = data
                    )
                }
            }.onError { error ->
                _state.update {
                    it.copy(
                        errorMessage = error.name,
                        isLoading = false,
                        characterDetails = null
                    )
                }
            }
        }
    }

}
// TODO: replace DTO with domain model

data class CharacterDetailsScreenState(
    val characterDetails: CharacterDto? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)