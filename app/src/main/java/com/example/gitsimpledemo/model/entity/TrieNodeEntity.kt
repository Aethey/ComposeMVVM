package com.example.gitsimpledemo.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Author: Ryu
 * Date: 2024/05/24
 * Description:
 */

@Entity(tableName = "trie_node")
data class TrieNodeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "parent_id") val parentId: Int? = null,
    @ColumnInfo(name = "character") val character: Char,
    @ColumnInfo(name = "is_end_of_word") val isEndOfWord: Boolean = false,
    @ColumnInfo(name = "timestamp") val timestamp: Long = System.currentTimeMillis()
)