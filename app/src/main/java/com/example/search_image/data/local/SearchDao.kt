package com.example.search_image.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.search_image.data.local.SearchDao.Constants.tableName

@Dao
interface SearchDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchListing(
        searchListingEntity: List<SearchListingEntity>
    )

    @Query("DELETE FROM $tableName")
    suspend fun clearSearchListings()

    @Query(
        """
            SELECT * 
            FROM $tableName
            WHERE LOWER(tags) LIKE '%' || LOWER(:query) || '%' OR
                UPPER(:query) == tags
        """
    )
    suspend fun querySearchListing(query: String): List<SearchListingEntity>

    object Constants {
        const val tableName = "searchListingEntity"
    }
}