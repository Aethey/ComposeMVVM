package com.example.gitsimpledemo.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.gitsimpledemo.model.entity.LanguageColorEntity

/**
 * Author: Ryu
 * Date: 2024/05/26
 * Description:
 */
@Dao
interface LanguageColorDao {

    @Query("SELECT COUNT(*) FROM language_colors")
    suspend fun count(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(colors: List<LanguageColorEntity>)

    @Query("SELECT color FROM language_colors WHERE language = :language")
    suspend fun getColorByLanguage(language: String): String?

    @Query("SELECT * FROM language_colors")
    suspend fun getAllColors(): List<LanguageColorEntity>
}