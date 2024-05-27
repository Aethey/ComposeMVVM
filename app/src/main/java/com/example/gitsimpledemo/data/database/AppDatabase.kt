package com.example.gitsimpledemo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.gitsimpledemo.model.dao.LanguageColorDao
import com.example.gitsimpledemo.model.dao.SearchHistoryDao
import com.example.gitsimpledemo.model.entity.LanguageColorEntity
import com.example.gitsimpledemo.model.entity.SearchHistoryEntity

/**
 * Author: Ryu
 * Date: 2024/05/24
 * Description:
 */

@Database(entities = [SearchHistoryEntity::class, LanguageColorEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun searchHistoryDao(): SearchHistoryDao
    abstract fun languageColorDao(): LanguageColorDao
}

val MIGRATION_1_2: Migration = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // 创建新的表
        database.execSQL("CREATE TABLE IF NOT EXISTS `language_colors` (`language` TEXT NOT NULL, `color` TEXT NOT NULL, PRIMARY KEY(`language`))")
    }
}