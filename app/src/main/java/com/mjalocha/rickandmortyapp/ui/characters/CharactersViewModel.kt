package com.mjalocha.rickandmortyapp.ui.characters

import android.util.Log
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjalocha.rickandmortyapp.R
import com.mjalocha.rickandmortyapp.data.model.Status
import com.mjalocha.rickandmortyapp.data.repository.CharacterRepository
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.onError
import com.mjalocha.rickandmortyapp.data.utils.onSuccess
import com.mjalocha.rickandmortyapp.ui.models.CharacterDetails
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@OptIn(FlowPreview::class)
@KoinViewModel
class CharactersViewModel(
    private val repository: CharacterRepository,
) : ViewModel() {
    private val _state =
        MutableStateFlow(
            CharactersScreenState(
                statusFilterChips = createStatusFilterChips(),
            ),
        )
    val state = _state.asStateFlow()

    private val searchQuery = MutableStateFlow("")
    private val selectedStatus = MutableStateFlow("")
    private var nextPage: Int? = null
    private var currentFetchJob: Job? = null
    private var lastPaginationRequestAt = 0L

    companion object {
        private const val PAGINATION_COOLDOWN_MS = 700L
    }

    init {
        viewModelScope.launch {
            combine(searchQuery, selectedStatus) { query, status ->
                query to status
            }.debounce(500)
                .collectLatest { (query, status) ->
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

    fun onStatusFilterChange(id: Int) {
        _state.update { state ->
            val updatedChips =
                state.statusFilterChips.map { chip ->
                    chip.copy(isSelected = chip.id == id)
                }
            selectedStatus.value = updatedChips.first { it.isSelected }.value
            state.copy(statusFilterChips = updatedChips)
        }
    }

    fun fetchCharacters() {
        if (_state.value.isLoading) return
        if (nextPage == null && !state.value.characters.isEmpty()) return
        if (currentFetchJob?.isActive == true) return

        currentFetchJob =
            viewModelScope.launch {
                val elapsed = System.currentTimeMillis() - lastPaginationRequestAt
                if (elapsed in 0 until PAGINATION_COOLDOWN_MS) {
                    delay(PAGINATION_COOLDOWN_MS - elapsed)
                }

                executeFetch(searchQuery.value, isInitial = false)
                lastPaginationRequestAt = System.currentTimeMillis()
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
                status = selectedStatus.value,
                name = query.ifEmpty { null },
            ).onSuccess { data ->
                nextPage = data.nextPage
                _state.update {
                    it.copy(
                        isLoading = false,
                        error = null,
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
                Log.e("CHARACTERS_VIEW_MODEL", "Error: ${error.name}")
                _state.update { it.copy(isLoading = false, error = error) }
            }
    }

    private fun createStatusFilterChips(): List<FilterButtonState> =
        listOf(
            FilterButtonState(
                id = 1,
                name = R.string.all,
                value = "",
                isSelected = true,
            ),
            FilterButtonState(
                id = 2,
                name = R.string.alive,
                value = Status.ALIVE.name.lowercase(),
                isSelected = false,
            ),
            FilterButtonState(
                id = 3,
                name = R.string.dead,
                value = Status.DEAD.name.lowercase(),
                isSelected = false,
            ),
            FilterButtonState(
                id = 4,
                name = R.string.unknown,
                value = Status.UNKNOWN.name.lowercase(),
                isSelected = false,
            ),
        )
}

data class CharactersScreenState(
    val characters: List<CharacterDetails> = emptyList(),
    val statusFilterChips: List<FilterButtonState>,
    val searchPhrase: String = "",
    val isLoading: Boolean = false,
    val error: DataError? = null,
)

data class FilterButtonState(
    val id: Int,
    @field:StringRes
    val name: Int,
    val value: String,
    val isSelected: Boolean = false,
)
