package com.example.gitsimpledemo.model.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Author: Ryu
 * Date: 2024/05/26
 * Description: LanguageColorEntity
 */
@Entity(tableName = "language_colors")
data class LanguageColorEntity(
    @PrimaryKey val language: String,
    val color: String
)