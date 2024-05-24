package com.example.gitsimpledemo.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitsimpledemo.model.entity.SearchHistoryEntity

/**
 * Author: Ryu
 * Date: 2024/05/24
 * Description:
 */

@Dao
interface SearchHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(searchHistoryEntity: SearchHistoryEntity): Long

    @Query("SELECT * FROM search_history ORDER BY timestamp DESC")
    suspend fun getAllHistorySortedByTime(): List<SearchHistoryEntity>
}