package com.example.search_image.data.remote.dto

import com.example.search_image.data.local.SearchListingEntity
import com.example.search_image.domain.entities.Search
import org.junit.Assert.assertEquals
import org.junit.Test

class SearchDtoTest {

    @Test
    fun `toSearch should convert SearchDto to Search`() {
        val dto = SearchDto(
            fullHDURL = "https://example.com/image.png",
            user = "John Doe",
            likes = 10,
            downloads = 5,
            comments = 3,
            id = 123,
            imageWidth = 1920f,
            imageHeight = 1080f,
            tags = "tag1,tag2",
            imageURL = "https://example.com/image.png",
            views = 100,
            previewURL = "https://example.com/preview.png",
            userId = 456,
            largeImageURL = "https://example.com/large.png",
            userImageURL = "https://example.com/user.png"
        )

        val expected = Search(
            fullHDURL = "https://example.com/image.png",
            user = "John Doe",
            likes = 10,
            downloads = 5,
            comments = 3,
            id = 123,
            imageWidth = 1920f,
            imageHeight = 1080f,
            tags = "tag1,tag2",
            imageURL = "https://example.com/image.png",
            views = 100,
            previewURL = "https://example.com/preview.png",
            userId = 456,
            largeImageURL = "https://example.com/large.png",
            userImageURL = "https://example.com/user.png"
        )

        assertEquals(expected, dto.toSearch())
    }

    @Test
    fun `toSearchEntity should convert SearchDto to SearchListingEntity`() {
        val dto = SearchDto(
            fullHDURL = "https://example.com/image.png",
            user = "John Doe",
            likes = 10,
            downloads = 5,
            comments = 3,
            id = 123,
            imageWidth = 1920f,
            imageHeight = 1080f,
            tags = "tag1,tag2",
            imageURL = "https://example.com/image.png",
            views = 100,
            previewURL = "https://example.com/preview.png",
            userId = 456,
            largeImageURL = "https://example.com/large.png",
            userImageURL = "https://example.com/user.png"
        )

        val expected = SearchListingEntity(
            fullHDURL = "https://example.com/image.png",
            user = "John Doe",
            likes = 10,
            downloads = 5,
            comments = 3,
            id = 123,
            imageWidth = 1920f,
            imageHeight = 1080f,
            tags = "tag1,tag2",
            imageURL = "https://example.com/image.png",
            views = 100,
            previewURL = "https://example.com/preview.png",
            userId = 456,
            largeImageURL = "https://example.com/large.png",
            userImageURL = "https://example.com/user.png"
        )

        assertEquals(expected, dto.toSearchEntity())
    }

}
