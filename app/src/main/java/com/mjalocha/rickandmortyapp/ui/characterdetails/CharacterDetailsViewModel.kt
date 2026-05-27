package com.mjalocha.rickandmortyapp.ui.characterdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjalocha.rickandmortyapp.data.repository.CharacterRepository
import com.mjalocha.rickandmortyapp.data.utils.onError
import com.mjalocha.rickandmortyapp.data.utils.onSuccess
import com.mjalocha.rickandmortyapp.ui.models.CharacterDetails
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class CharacterDetailsViewModel(
    @InjectedParam val characterId: Int,
    private val repository: CharacterRepository,
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
                isLoading = true,
            )
        }
        characterFetchJob?.cancel()
        characterFetchJob =
            viewModelScope.launch {
                repository
                    .getCharacter(id = characterId)
                    .onSuccess { data ->
                        _state.update {
                            it.copy(
                                isLoading = false,
                                errorMessage = null,
                                characterDetails = data,
                            )
                        }
                    }.onError { error ->
                        _state.update {
                            it.copy(
                                errorMessage = error.name,
                                isLoading = false,
                                characterDetails = null,
                            )
                        }
                    }
            }
    }
}

data class CharacterDetailsScreenState(
    val characterDetails: CharacterDetails? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
