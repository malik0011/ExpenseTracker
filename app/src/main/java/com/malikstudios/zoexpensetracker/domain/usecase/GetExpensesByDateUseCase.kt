package com.malikstudios.zoexpensetracker.domain.usecase

import com.malikstudios.zoexpensetracker.domain.repository.ExpenseRepository

/**
 * Use case for retrieving expenses by a specific date.
 * This encapsulates the logic for fetching expenses, allowing for easier testing and separation of concerns.
 */
class GetExpensesByDateUseCase(private val repository: ExpenseRepository) {
    operator fun invoke(date: String) = repository.getExpensesByDate(date)
}
