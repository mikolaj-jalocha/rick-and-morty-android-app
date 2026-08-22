package com.mjalocha.rickandmortyapp.ui.models

import com.mjalocha.rickandmortyapp.data.model.CharacterResponse
import com.mjalocha.rickandmortyapp.data.model.ResponseMetaData
import com.mjalocha.rickandmortyapp.data.model.dto.CharacterDto
import com.mjalocha.rickandmortyapp.data.model.dto.LocationDto
import com.mjalocha.rickandmortyapp.data.model.dto.OriginDto
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class CharacterDetailsTest {
    @Test
    fun `extractPageNumber null input`() {
        val nullInput: String? = null
        val actual = nullInput.extractPageNumber()

        assertNull(actual)
    }

    @Test
    fun `extractPageNumber missing page parameter`() {
        val input = "https://rickandmortyapi.com/api/character?gender=male&status=dead"
        val actual = input.extractPageNumber()
        assertNull(actual)
    }

    @Test
    fun `extractPageNumber valid page at end of string`() {
        val input = "https://rickandmortyapi.com/api/?page=2"
        val actual = input.extractPageNumber()

        assertEquals(2, actual)
    }

    @Test
    fun `extractPageNumber valid page with trailing parameters`() {
        val input = "https://rickandmortyapi.com/api/character?page=2&gender=male&status=dead"
        val actual = input.extractPageNumber()

        assertEquals(2, actual)
    }

    @Test
    fun `extractPageNumber non numeric page value`() {
        val input = "https://rickandmortyapi.com/api/?page=abc"
        val actual = input.extractPageNumber()

        assertNull(actual)
    }

    @Test
    fun `extractPageNumber empty page value`() {
        val input = "https://rickandmortyapi.com/api/?page="
        val actual = input.extractPageNumber()

        assertNull(actual)
    }

    @Test
    fun `extractPageNumber integer overflow`() {
        val input = "https://rickandmortyapi.com/api/?page=2147483648"
        val actual = input.extractPageNumber()

        assertNull(actual)
    }

    @Test
    fun `toCharacterData mapping with null next page`() {
        val response =
            CharacterResponse(
                info =
                    ResponseMetaData(
                        count = 0,
                        pages = 0,
                        next = null,
                        prev = null,
                    ),
                results = emptyList(),
            )

        val result = response.toCharacterData().nextPage

        assertNull(result)
    }

    @Test
    fun `toCharacterData mapping with empty results`() {
        val response =
            CharacterResponse(
                info =
                    ResponseMetaData(
                        count = 0,
                        pages = 0,
                        next = null,
                        prev = null,
                    ),
                results = emptyList(),
            )

        val result = response.toCharacterData().results.isEmpty()

        assertTrue(result)
    }

    @Test
    fun `toCharacterData mapping with valid data`() {
        val response =
            CharacterResponse(
                info =
                    ResponseMetaData(
                        count = 0,
                        pages = 0,
                        next = "https://rickandmortyapi.com/api/?page=2",
                        prev = "https://rickandmortyapi.com/api/?page=1",
                    ),
                results =
                    listOf(
                        CharacterDto(
                            id = 1,
                            name = "",
                            status = "",
                            species = "",
                            type = "",
                            gender = "",
                            origin = OriginDto("", ""),
                            location = LocationDto("", ""),
                            image = "",
                            episode = emptyList(),
                            url = "",
                            created = "",
                        ),
                        CharacterDto(
                            id = 2,
                            name = "",
                            status = "",
                            species = "",
                            type = "",
                            gender = "",
                            origin = OriginDto("", ""),
                            location = LocationDto("", ""),
                            image = "",
                            episode = emptyList(),
                            url = "",
                            created = "",
                        ),
                    ),
            )
        val parsed = response.toCharacterData()

        assertTrue(parsed.results.isNotEmpty())
        assertEquals(2, parsed.nextPage)
    }
}
