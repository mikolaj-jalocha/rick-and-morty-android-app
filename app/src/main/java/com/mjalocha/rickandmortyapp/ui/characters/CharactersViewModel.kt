package com.mjalocha.rickandmortyapp.ui.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjalocha.rickandmortyapp.data.repository.CharacterRepository
import com.mjalocha.rickandmortyapp.data.utils.onError
import com.mjalocha.rickandmortyapp.data.utils.onSuccess
import com.mjalocha.rickandmortyapp.ui.models.CharacterDetails
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@OptIn(FlowPreview::class)
@KoinViewModel
class CharactersViewModel(
    private val repository: CharacterRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(CharactersScreenState())
    val state = _state.asStateFlow()

    private val searchQuery = MutableStateFlow("")
    private var nextPage: Int? = null
    private var currentFetchJob: Job? = null

    init {
        viewModelScope.launch {
            searchQuery
                .debounce(500)
                .collectLatest { query ->
                    currentFetchJob?.cancel()
                    nextPage = null
                    executeFetch(query, isInitial = true)
                }
        }
    }

    fun onSearchQueryChange(query: String) {
        searchQuery.value = query
        _state.update {
            it.copy(searchPhrase = query)
        }
    }

    fun fetchCharacters() {
        if (_state.value.isLoading || (searchQuery.value.isNotEmpty() && nextPage == null)) return

        currentFetchJob?.cancel()
        currentFetchJob =
            viewModelScope.launch {
                executeFetch(searchQuery.value, isInitial = false)
            }
    }

    private suspend fun executeFetch(
        query: String,
        isInitial: Boolean,
    ) {
        _state.update { it.copy(isLoading = true) }

        repository
            .getCharacters(
                page = nextPage ?: 1,
                name = query.ifEmpty { null },
            ).onSuccess { data ->
                nextPage = data.nextPage
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = null,
                        characters =
                            if (isInitial) {
                                data.results
                            } else {
                                (it.characters + data.results).distinctBy { character ->
                                    character.id
                                }
                            },
                    )
                }
            }.onError { error ->
                _state.update { it.copy(isLoading = false, errorMessage = error.name) }
            }
    }
}

data class CharactersScreenState(
    val characters: List<CharacterDetails> = emptyList(),
    val searchPhrase: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)
