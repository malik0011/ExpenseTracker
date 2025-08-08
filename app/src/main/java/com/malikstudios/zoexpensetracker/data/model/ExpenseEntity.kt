package com.malikstudios.zoexpensetracker.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/*
 * ExpenseEntity represents an expense record in the database.
 */
@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey val id: String,
    val title: String,
    val amountInPaise: Long,
    val category: String,
    val notes: String?,
    val receiptUrl: String?,
    val timestampMillis: Long,
    val date: String
)