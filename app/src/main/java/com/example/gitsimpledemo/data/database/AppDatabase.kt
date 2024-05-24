package com.example.gitsimpledemo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.gitsimpledemo.model.dao.SearchHistoryDao
import com.example.gitsimpledemo.model.entity.SearchHistoryEntity

/**
 * Author: Ryu
 * Date: 2024/05/24
 * Description:
 */

@Database(entities = [SearchHistoryEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
}