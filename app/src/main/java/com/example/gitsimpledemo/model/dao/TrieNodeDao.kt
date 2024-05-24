package com.example.gitsimpledemo.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitsimpledemo.model.entity.TrieNodeEntity

/**
 * Author: Ryu
 * Date: 2024/05/24
 * Description:
 */

@Dao
interface TrieNodeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(node: TrieNodeEntity): Long

    @Query("SELECT * FROM trie_node WHERE parent_id = :parentId AND character = :character LIMIT 1")
    suspend fun getNode(parentId: Int?, character: Char): TrieNodeEntity?

    @Query("SELECT * FROM trie_node WHERE parent_id = :parentId")
    suspend fun getChildren(parentId: Int?): List<TrieNodeEntity>

    @Query("SELECT * FROM trie_node WHERE is_end_of_word = 1 ORDER BY timestamp DESC")
    suspend fun getAllEndOfWordNodesSortedByTime(): List<TrieNodeEntity>

    @Query("SELECT * FROM trie_node")
    suspend fun getAllNodes(): List<TrieNodeEntity>
}