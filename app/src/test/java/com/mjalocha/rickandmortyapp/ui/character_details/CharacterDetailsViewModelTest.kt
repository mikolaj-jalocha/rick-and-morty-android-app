package com.mjalocha.rickandmortyapp.ui.character_details

import com.mjalocha.rickandmortyapp.data.model.dto.LocationDto
import com.mjalocha.rickandmortyapp.data.model.dto.OriginDto
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result
import com.mjalocha.rickandmortyapp.mock.FakeCharacterRepository
import com.mjalocha.rickandmortyapp.testutil.MainDispatcherRule
import com.mjalocha.rickandmortyapp.ui.models.CharacterDetails
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `init sets loading true before fetch completes and false after success`() = runTest {
        val repository = FakeCharacterRepository(
            getCharacterResult = Result.Success(sampleCharacter())
        )

        val viewModel = CharacterDetailsViewModel(characterId = 1, repository = repository)

        assertTrue(viewModel.state.value.isLoading)

        advanceUntilIdle()

        assertFalse(viewModel.state.value.isLoading)
    }

    @Test
    fun `init fetches character details on success`() = runTest {
        val expected = sampleCharacter()
        val repository = FakeCharacterRepository(
            getCharacterResult = Result.Success(expected)
        )

        val viewModel = CharacterDetailsViewModel(characterId = 42, repository = repository)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertNull(state.errorMessage)
        assertEquals(expected, state.characterDetails)
        assertEquals(listOf(42), repository.requestedCharacterIds)
    }

    @Test
    fun `init sets error state when repository returns error`() = runTest {
        val repository = FakeCharacterRepository(
            getCharacterResult = Result.Error(DataError.Remote.NO_INTERNET)
        )

        val viewModel = CharacterDetailsViewModel(characterId = 7, repository = repository)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertFalse(state.isLoading)
        assertEquals(DataError.Remote.NO_INTERNET.name, state.errorMessage)
        assertNull(state.characterDetails)
        assertEquals(listOf(7), repository.requestedCharacterIds)
    }

    private fun sampleCharacter() = CharacterDetails(
        id = 1,
        name = "Rick Sanchez",
        status = "Alive",
        species = "Human",
        gender = "Male",
        origin = OriginDto(name = "Earth", url = ""),
        location = LocationDto(name = "Earth", url = ""),
        image = "",
        episode = emptyList()
    )
}
