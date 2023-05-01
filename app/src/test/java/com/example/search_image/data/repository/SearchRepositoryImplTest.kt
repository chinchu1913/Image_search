package com.example.search_image.data.repository

import com.example.search_image.common.Resource
import com.example.search_image.data.local.SearchDao
import com.example.search_image.data.local.SearchDatabase
import com.example.search_image.data.local.SearchListingEntity
import com.example.search_image.data.remote.SearchApi
import com.example.search_image.data.remote.dto.SearchDto
import com.example.search_image.data.remote.dto.SearchResultDto
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito
import org.mockito.kotlin.mock
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException

class SearchRepositoryImplTest {
    private lateinit var mockSearchApi: SearchApi
    private lateinit var repository: SearchRepositoryImpl
    private lateinit var mockLocalDb: SearchDatabase
    private lateinit var mockDao: SearchDao
    private lateinit var sampleSearchEntity: SearchListingEntity

    @Before
    fun setUp() {
        mockSearchApi = mock()
        mockLocalDb = mock()
        mockDao = mock()
        repository = SearchRepositoryImpl(mockSearchApi, mockLocalDb)
        sampleSearchEntity = SearchListingEntity(
            comments = 0,
            downloads = 0,
            fullHDURL = "String",
            id = 1,
            imageWidth = 5f,
            imageHeight = 5f,
            imageURL = "imageURL",
            largeImageURL = "largeImageURL",
            likes = 4,
            previewURL = "previewURL",
            tags = "tags",
            user = "user",
            userImageURL = "userImageURL",
            userId = 2345,
            views = 10,
        )
    }

    private fun getResultDto() = SearchDto(
        comments = 0,
        downloads = 0,
        fullHDURL = "String",
        id = 1,
        imageWidth = 5f,
        imageHeight = 5f,
        imageURL = "imageURL",
        largeImageURL = "largeImageURL",
        likes = 4,
        previewURL = "previewURL",
        tags = "tags",
        user = "user",
        userImageURL = "userImageURL",
        userId = 2345,
        views = 10,
    )

    private fun getDtoData() = SearchResultDto(
        hits = listOf(
            getResultDto()
        ),
        total = 100,
        totalHits = 10
    )

    @Test
    fun `is loading is true on getSearchList`() = runBlocking {
        Mockito.`when`(mockSearchApi.getSearchList("fruits")).thenReturn(null)
        val firstItem = repository.getSearchResults("fruits").first()
        assertThat((firstItem as Resource.Loading).isLoading).isEqualTo(true)
    }


    @Test
    fun `getSearchList returns Success DB`() = runBlocking {
        val expectedData = sampleSearchEntity

        Mockito.`when`(mockLocalDb.dao).thenReturn(mockDao)

        Mockito.`when`(mockDao.querySearchListing("fruits")).thenReturn(listOf(sampleSearchEntity))


        val secondItem = repository
            .getSearchResults("fruits").drop(1).first()
        assertThat(secondItem).isInstanceOf(Resource.Success::class.java)

        assertThat((secondItem as Resource.Success).data?.first()?.downloads).isEqualTo(expectedData.downloads)
        assertThat(secondItem.data?.first()?.id).isEqualTo(expectedData.id)
        assertThat(secondItem.data?.first()?.comments)
            .isEqualTo(expectedData.comments)
    }

    @Test
    fun `getSearchList returns Success API`() = runBlocking {

        val expectedData = sampleSearchEntity

        Mockito.`when`(mockSearchApi.getSearchList("fruits")).thenReturn(getDtoData())

        Mockito.`when`(mockLocalDb.dao).thenReturn(mockDao)

        Mockito.`when`(mockDao.querySearchListing("fruits")).thenReturn(emptyList())


        val thirdItem = repository
            .getSearchResults("fruits").drop(2).first()
        assertThat(thirdItem).isInstanceOf(Resource.Success::class.java)

        assertThat((thirdItem as Resource.Success).data?.first()?.downloads).isEqualTo(expectedData.downloads)
        assertThat(thirdItem.data?.first()?.id).isEqualTo(expectedData.id)
        assertThat(thirdItem.data?.first()?.comments)
            .isEqualTo(expectedData.comments)
    }

    @Test
    fun `getSearchList db insert`() = runBlocking {
        Mockito.`when`(mockSearchApi.getSearchList("fruits")).thenReturn(getDtoData())

        Mockito.`when`(mockLocalDb.dao).thenReturn(mockDao)

        Mockito.`when`(mockDao.querySearchListing("fruits")).thenReturn(emptyList())

        val thirdItem = repository
            .getSearchResults("fruits").drop(2).first()

        verify(mockDao, times(1)).insertSearchListing(listOf(sampleSearchEntity))

    }

}