package com.mjalocha.rickandmortyapp.ui.characters

import com.mjalocha.rickandmortyapp.data.model.dto.LocationDto
import com.mjalocha.rickandmortyapp.data.model.dto.OriginDto
import com.mjalocha.rickandmortyapp.data.utils.DataError
import com.mjalocha.rickandmortyapp.data.utils.Result
import com.mjalocha.rickandmortyapp.mock.FakeCharacterRepository
import com.mjalocha.rickandmortyapp.testutil.MainDispatcherRule
import com.mjalocha.rickandmortyapp.ui.models.CharacterDetails
import com.mjalocha.rickandmortyapp.ui.models.CharactersData
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class CharactersViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `init fetches characters and updates state on success`() =
        runTest {
            val expected = listOf(character(id = 1), character(id = 2))
            val repository =
                FakeCharacterRepository(
                    defaultResult = Result.Success(charactersData(expected)),
                )

            val viewModel = CharactersViewModel(repository)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertFalse(state.isLoading)
            assertNull(state.errorMessage)
            assertEquals(expected, state.characters)
            assertEquals(1, repository.getCharactersCalls.size)
            assertNull(repository.getCharactersCalls.single().name)
        }

    @Test
    fun `init sets error state when repository returns error`() =
        runTest {
            val repository =
                FakeCharacterRepository(
                    defaultResult = Result.Error(DataError.Remote.SERVER),
                )

            val viewModel = CharactersViewModel(repository)
            advanceUntilIdle()

            val state = viewModel.state.value
            assertFalse(state.isLoading)
            assertEquals(DataError.Remote.SERVER.name, state.errorMessage)
            assertTrue(state.characters.isEmpty())
        }

    @Test
    fun `search query is debounced and only latest query is executed`() =
        runTest {
            val repository =
                FakeCharacterRepository(
                    defaultResult = Result.Success(charactersData(emptyList())),
                ).apply {
                    resultByName["Rick"] =
                        Result.Success(
                            charactersData(
                                listOf(
                                    character(
                                        id = 10,
                                        name = "Rick Sanchez",
                                    ),
                                ),
                            ),
                        )
                    resultByName["Ri"] =
                        Result.Success(charactersData(listOf(character(id = 11, name = "Ri"))))
                }

            val viewModel = CharactersViewModel(repository)
            advanceUntilIdle()
            repository.clearCalls()

            viewModel.onSearchQueryChange("Ri")
            advanceTimeBy(300)
            viewModel.onSearchQueryChange("Rick")

            advanceTimeBy(499)
            assertTrue(repository.getCharactersCalls.isEmpty())

            advanceTimeBy(1)
            advanceUntilIdle()

            val calls = repository.getCharactersCalls
            assertEquals(1, calls.size)
            assertEquals("Rick", calls.single().name)

            val state = viewModel.state.value
            assertEquals("Rick", state.searchPhrase)
            assertEquals(listOf(character(id = 10, name = "Rick Sanchez")), state.characters)
            assertNull(state.errorMessage)
            assertFalse(state.isLoading)
        }

    private fun character(
        id: Int,
        name: String = "Character $id",
    ) = CharacterDetails(
        id = id,
        name = name,
        status = "Alive",
        species = "Human",
        gender = "Male",
        origin = OriginDto("Earth", ""),
        location = LocationDto("Earth", ""),
        image = "",
        episode = emptyList(),
    )

    private fun charactersData(characters: List<CharacterDetails>) =
        CharactersData(
            nextPage = null,
            results = characters,
        )
}
