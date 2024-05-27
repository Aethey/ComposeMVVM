package com.example.gitsimpledemo.util

import java.util.Locale

/**
 * Author: Ryu
 * Date: 2024/05/26
 * Description: big number format
 */
object CommonUtils {
    //format too long number
    fun formatNumber(number: Long): String {
        return when {
            number < 1000 -> number.toString()
            number < 1_000_000 -> String.format(Locale.ROOT, "%.1fK", number / 1000.0)
            number < 1_000_000_000 -> String.format(Locale.ROOT, "%.1fM", number / 1_000_000.0)
            else -> String.format(Locale.ROOT, "%.1fB", number / 1_000_000_000.0)
        }
    }

    // format date
    fun convertIsoToSimpleDate(isoDate: String): String {
        val dateRegex = Regex("""\d{4}-\d{2}-\d{2}""")
        return dateRegex.find(isoDate)?.value ?: "Invalid date format"
    }
}