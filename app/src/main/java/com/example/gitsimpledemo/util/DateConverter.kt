package com.example.gitsimpledemo.util

/**
 * Author: Ryu
 * Date: 2024/05/26
 * Description:
 */

object DateConverter {
    fun convertIsoToSimpleDate(isoDate: String): String {
        val dateRegex = Regex("""\d{4}-\d{2}-\d{2}""")
        return dateRegex.find(isoDate)?.value ?: "Invalid date format"
    }
}