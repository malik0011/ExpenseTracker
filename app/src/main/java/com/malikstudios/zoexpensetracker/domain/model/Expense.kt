package com.malikstudios.zoexpensetracker.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * amountInPaise: store currency as Long representing paise.
 * This avoids floating point rounding issues.
 */
data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amountInPaise: Long,
    val category: Category,
    val notes: String? = null,
    val receiptImageUrl: String? = null,
    val timestampMillis: Long = System.currentTimeMillis(),
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        .format(Date(System.currentTimeMillis()))
)

enum class Category {
    Staff, Travel, Food, Utility
}