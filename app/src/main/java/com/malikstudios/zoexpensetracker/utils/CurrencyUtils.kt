package com.malikstudios.zoexpensetracker.utils

import kotlin.math.abs

/*
    * Utility object for currency-related functions.
 */
object CurrencyUtils {
    /**
     * Parses a string representing an amount in rupees and paisa to a total amount in paise.
     * The input can be in the format "123.45", "123", "-123.45", etc.
     * Returns a Result<Long> where the value is the total amount in paise.
     * If the input is invalid, it returns a failure with an IllegalArgumentException.
     */
    fun parseToPaise(amountText: String): Result<Long> {
        val s = amountText.trim()
        if (s.isEmpty()) return Result.failure(IllegalArgumentException("Empty amount"))
        val negative = s.startsWith("-")
        val clean = if (negative) s.substring(1) else s
        return try {
            val parts = clean.split('.', limit = 2)
            val rupeesPart = parts.getOrNull(0)?.filter { it.isDigit() } ?: "0"
            val paisaPartRaw = parts.getOrNull(1) ?: ""
            val paisaPart = when {
                paisaPartRaw.length >= 2 -> paisaPartRaw.substring(0, 2)
                paisaPartRaw.length == 1 -> paisaPartRaw + "0"
                else -> "00"
            }
            val total = rupeesPart.toLong() * 100 + paisaPart.toLong()
            if (negative) Result.success(-total) else Result.success(total)
        } catch (e: NumberFormatException) {
            Result.failure(IllegalArgumentException("Invalid amount format"))
        }
    }

    /**
     * Formats a given amount in paise to a string representation in rupees and paisa.
     * The output is in the format "₹X.YY" where X is the rupee part and YY is the paisa part.
     * If the input is negative, it will include a '-' sign before the rupee symbol.
     */
    fun formatPaiseToRupeeString(paise: Long): String {
        val negative = paise < 0
        val absPaise = abs(paise)
        val rupees = absPaise / 100
        val p = absPaise % 100
        val paisaStr = if (p < 10) "0$p" else "$p"
        return (if (negative) "-" else "") + "₹$rupees.$paisaStr"
    }
}
