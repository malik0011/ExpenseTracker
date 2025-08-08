package com.malikstudios.zoexpensetracker.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Utility object for date-related functions.
 */
object DateUtils {
    /**
     * Returns the current date formatted as "yyyy-MM-dd".
     * This is useful for displaying today's date in a consistent format.
     */
    fun todayDateString(): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(System.currentTimeMillis()))
}