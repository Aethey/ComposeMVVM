package com.example.gitsimpledemo.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Author: Ryu
 * Date: 2024/05/24
 * Description:
 */

@Entity(
    tableName = "search_history",
    indices = [Index(value = ["search_query"], unique = true)]
)
data class SearchHistoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "search_query") val searchQuery: String,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "type") val type: SearchType
)

enum class SearchType {
    USERNAME,
    PROJECT_NAME
}

enum class SearchActionType {
    KEYBOARD,
    CLICK
}

enum class SearchViewType {
    CLEAR_CLOSE,
    COMMON_CLOSE,
    SEARCH_CLOSE,
    OPEN
}