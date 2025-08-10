package com.malikstudios.zoexpensetracker.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Enhanced Expense model with multi-currency support
 * amountInSmallestUnit: store currency as Long representing smallest unit (paise/cents/etc).
 * This avoids floating point rounding issues.
 */
data class Expense(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val amountInSmallestUnit: Long, // Renamed for clarity - was amountInPaise
    val category: Category = Category.Other,
    val notes: String? = null,
    val receiptImageUrl: String? = null,
    val timestampMillis: Long = System.currentTimeMillis(),
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        .format(Date(System.currentTimeMillis()))
) {
    // Legacy property for backward compatibility
    @Deprecated("Use amountInSmallestUnit instead", ReplaceWith("amountInSmallestUnit"))
    val amountInPaise: Long get() = amountInSmallestUnit
}

enum class Category {
    Staff, Travel, Food, Utility, Other
}