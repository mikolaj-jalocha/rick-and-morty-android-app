package com.mjalocha.rickandmortyapp.ui.episode_details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mjalocha.rickandmortyapp.data.repository.EpisodeRepository
import com.mjalocha.rickandmortyapp.data.utils.onError
import com.mjalocha.rickandmortyapp.data.utils.onSuccess
import com.mjalocha.rickandmortyapp.ui.models.Episode
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.InjectedParam
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class EpisodeDetailsViewModel(
    @InjectedParam val episodeId: Int,
    private val repository: EpisodeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EpisodeDetailsScreenState())
    val state = _state.asStateFlow()

    private var episodeFetchJob: Job? = null

    init {
        fetchEpisode()
    }

    private fun fetchEpisode() {
        _state.update { it.copy(isLoading = true) }
        episodeFetchJob?.cancel()
        episodeFetchJob = viewModelScope.launch {
            repository.getEpisode(episodeId).onSuccess { data ->
                _state.update { it.copy(isLoading = false, episode = data, errorMessage = null) }
            }.onError { error ->
                _state.update { it.copy(isLoading = false, episode = null, errorMessage = error.name) }
            }
        }
    }
}

data class EpisodeDetailsScreenState(
    val episode: Episode? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)