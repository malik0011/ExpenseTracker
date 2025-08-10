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

    fun fromMillisToDateString(millis: Long): String =
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))
    
    /**
     * Returns a list of formatted dates for the last N days.
     * This is useful for chart displays and date ranges.
     */
    fun getLastNDaysFormatted(n: Int): List<String> {
        val dates = mutableListOf<String>()
        val calendar = java.util.Calendar.getInstance()
        
        repeat(n) { dayOffset ->
            calendar.add(java.util.Calendar.DAY_OF_YEAR, -dayOffset)
            dates.add(0, SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time))
            calendar.add(java.util.Calendar.DAY_OF_YEAR, dayOffset) // Reset to original date
        }
        
        return dates
    }
}