package com.malikstudios.zoexpensetracker.domain.repository

import com.malikstudios.zoexpensetracker.data.local.ExpenseDao
import com.malikstudios.zoexpensetracker.data.toDomain
import com.malikstudios.zoexpensetracker.data.toEntity
import com.malikstudios.zoexpensetracker.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

/**
 * ExpenseRepositoryImpl is the implementation of the ExpenseRepository interface.
 * It provides methods to add expenses and retrieve them by date or all expenses.
 * * @param dao The ExpenseDao instance for database operations.
 */
class ExpenseRepositoryImpl @Inject constructor(
    private val dao: ExpenseDao
) : ExpenseRepository {

    override suspend fun addExpense(expense: Expense) {
        dao.insert(expense.toEntity())
    }

    override fun getExpensesByDate(date: String): Flow<List<Expense>> =
        dao.getExpensesByDate(date).map { list -> list.map { it.toDomain() } }

    override fun getExpensesByDateRange(startDate: String, endDate: String): Flow<List<Expense>> =
        dao.getExpensesByDateRange(startDate, endDate).map { list -> list.map { it.toDomain() } }

    override fun getAllExpenses(): Flow<List<Expense>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override fun getTotalForDate(date: String): Flow<Long> {
        return dao.getTotalForDate(date).map { it } // Return 0 if null
    }


}