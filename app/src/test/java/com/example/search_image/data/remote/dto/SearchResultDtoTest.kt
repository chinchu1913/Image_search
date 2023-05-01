package com.example.search_image.data.remote.dto

import org.junit.Assert.assertEquals
import org.junit.Test

class SearchResultDtoTest {

    @Test
    fun testSearchResultDto() {
        val search1 = SearchDto(
            fullHDURL = "http://example.com/image1",
            user = "John",
            likes = 10,
            downloads = 5,
            comments = 2,
            id = 1,
            imageWidth = 1920f,
            imageHeight = 1080f,
            tags = "tag1,tag2",
            imageURL = "http://example.com/image1.jpg",
            views = 100,
            previewURL = "http://example.com/preview1.jpg",
            userId = 123,
            largeImageURL = "http://example.com/large1.jpg",
            userImageURL = "http://example.com/user1.jpg"
        )
        val search2 = SearchDto(
            fullHDURL = "http://example.com/image2",
            user = "Jane",
            likes = 20,
            downloads = 10,
            comments = 3,
            id = 2,
            imageWidth = 1080f,
            imageHeight = 1920f,
            tags = "tag3,tag4",
            imageURL = "http://example.com/image2.jpg",
            views = 200,
            previewURL = "http://example.com/preview2.jpg",
            userId = 456,
            largeImageURL = "http://example.com/large2.jpg",
            userImageURL = "http://example.com/user2.jpg"
        )
        val hits = listOf(search1, search2)
        val total = 2
        val totalHits = 10

        val result = SearchResultDto(hits, total, totalHits)

        assertEquals(hits, result.hits)
        assertEquals(total, result.total)
        assertEquals(totalHits, result.totalHits)

        val convertedSearches = hits.map { it.toSearch() }
        assertEquals(convertedSearches[0].fullHDURL, "http://example.com/image1")
        assertEquals(convertedSearches[0].user, "John")
        assertEquals(convertedSearches[0].likes, 10)
        assertEquals(convertedSearches[0].downloads, 5)
        assertEquals(convertedSearches[0].comments, 2)
        assertEquals(convertedSearches[0].id, 1)
        assertEquals(convertedSearches[0].imageWidth, 1920f, 0.001f)
        assertEquals(convertedSearches[0].imageHeight, 1080f, 0.001f)
        assertEquals(convertedSearches[0].tags, "tag1,tag2")
        assertEquals(convertedSearches[0].imageURL, "http://example.com/image1.jpg")
        assertEquals(convertedSearches[0].views, 100)
        assertEquals(convertedSearches[0].previewURL, "http://example.com/preview1.jpg")
        assertEquals(convertedSearches[0].userId, 123)
        assertEquals(convertedSearches[0].largeImageURL, "http://example.com/large1.jpg")
        assertEquals(convertedSearches[0].userImageURL, "http://example.com/user1.jpg")

        assertEquals(convertedSearches[1].fullHDURL, "http://example.com/image2")
        assertEquals(convertedSearches[1].user, "Jane")
        assertEquals(convertedSearches[1].likes, 20)
        assertEquals(convertedSearches[1].downloads, 10)
    }
}