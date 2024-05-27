package com.example.gitsimpledemo.util

import android.content.Context
import androidx.room.Room
import com.example.gitsimpledemo.data.database.AppDatabase
import com.example.gitsimpledemo.model.entity.LanguageColorEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

/**
 * Author: Ryu
 * Date: 2024/05/26
 * Description: manage language color
 * base data json file in assets, save to db when first time
 * color base file get from:
 * https://github.com/github-linguist/linguist/blob/master/lib/linguist/languages.yml
 */
object LanguageColorManager {
    private var colorMap: MutableMap<String, String> = mutableMapOf()
    private lateinit var database: AppDatabase

    fun initialize(context: Context) {
        database = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "app_database"
        ).build()

        CoroutineScope(Dispatchers.IO).launch {
            if (isDatabaseEmpty()) {
                loadColorsFromJson(context)
            }
            loadColorsIntoMemory()
        }
    }

    private suspend fun isDatabaseEmpty(): Boolean {
        return database.languageColorDao().count() == 0
    }

    private suspend fun loadColorsFromJson(context: Context) {
        val jsonString =
            context.assets.open("language_color.json").bufferedReader().use { it.readText() }
        val jsonObject = JSONObject(jsonString)
        val colorList = mutableListOf<LanguageColorEntity>()
        val iter = jsonObject.keys()
        while (iter.hasNext()) {
            val language = iter.next()
            val color = jsonObject.getString(language)
            colorMap[language] = color  // Directly store to the map
            colorList.add(LanguageColorEntity(language, color))
        }
        database.languageColorDao().insertAll(colorList)
    }

    private suspend fun loadColorsIntoMemory() {
        val colors = database.languageColorDao().getAllColors()
        colorMap.clear()
        colorMap.putAll(colors.associate { it.language to it.color })
    }

    fun getColor(language: String): String {
        return colorMap[language] ?: "#000000" // default BLACK
    }

    fun getAdvancedContrastColor(hexColor: String): String {

        val cleanHex = hexColor.removePrefix("#")
        if (cleanHex.length != 6) {
            throw IllegalArgumentException("Invalid hex color length. Color should be six characters long.")
        }

        val r = cleanHex.substring(0, 2).toInt(16)
        val g = cleanHex.substring(2, 4).toInt(16)
        val b = cleanHex.substring(4, 6).toInt(16)

        val contrastR = 255 - r
        val contrastG = 255 - g
        val contrastB = 255 - b

        return String.format("#%02x%02x%02x", contrastR, contrastG, contrastB)
    }
}