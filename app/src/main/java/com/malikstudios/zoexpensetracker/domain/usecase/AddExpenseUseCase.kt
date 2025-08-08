package com.malikstudios.zoexpensetracker.domain.usecase

import com.malikstudios.zoexpensetracker.domain.model.Expense
import com.malikstudios.zoexpensetracker.domain.repository.ExpenseRepository

/**
 * Use case for adding an expense to the repository.
 * This encapsulates the logic for adding an expense, allowing for easier testing and separation of concerns.
 */
class AddExpenseUseCase(private val repository: ExpenseRepository) {
    suspend operator fun invoke(expense: Expense) = repository.addExpense(expense)
}