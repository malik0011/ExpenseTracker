package com.malikstudios.zoexpensetracker.data

import com.malikstudios.zoexpensetracker.data.model.ExpenseEntity
import com.malikstudios.zoexpensetracker.domain.model.Category
import com.malikstudios.zoexpensetracker.domain.model.Expense

/**
 * Extension functions to convert between domain model [Expense] and data model [ExpenseEntity].
 * This is necessary for Room database operations.
 */
fun Expense.toEntity() = ExpenseEntity(
    id = id,
    title = title,
    amountInPaise = amountInPaise,
    category = category.name,
    notes = notes,
    receiptUrl = receiptImageUrl,
    timestampMillis = timestampMillis,
    date = date
)

/**
 * Converts an [ExpenseEntity] to a domain model [Expense].
 * Uses runCatching to safely convert the category string to the Category enum.
 */
fun ExpenseEntity.toDomain() = Expense(
    id = id,
    title = title,
    amountInSmallestUnit = amountInPaise,
    category = Category.entries.find { it.name == category } ?: Category.Other ,
    notes = notes,
    receiptImageUrl = receiptUrl,
    timestampMillis = timestampMillis,
    date = date
)