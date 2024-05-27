package com.example.gitsimpledemo.util

import java.util.Locale

/**
 * Author: Ryu
 * Date: 2024/05/26
 * Description:
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
}