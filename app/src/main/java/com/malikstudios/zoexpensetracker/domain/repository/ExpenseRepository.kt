package com.malikstudios.zoexpensetracker.domain.repository

import com.malikstudios.zoexpensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow

/*
 * ExpenseRepository interface defines the contract for expense-related operations.
 * It provides methods to add expenses and retrieve them by date or all expenses.
 */
interface ExpenseRepository {
    suspend fun addExpense(expense: Expense)
    fun getExpensesByDate(date: String): Flow<List<Expense>>
    fun getAllExpenses(): Flow<List<Expense>>

    fun getTotalForDate(date: String): Flow<Long> // Returns total amount for a specific date in smallest currency unit
}