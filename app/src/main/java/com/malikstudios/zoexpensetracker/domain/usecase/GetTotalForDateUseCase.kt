package com.malikstudios.zoexpensetracker.domain.usecase

import com.malikstudios.zoexpensetracker.domain.repository.ExpenseRepository

/**
 * Use case for retrieving the total expenses for a specific date.
 * This encapsulates the logic for fetching the total, allowing for easier testing and separation of concerns.
 */
class GetTotalForDateUseCase(private val repository: ExpenseRepository) {
    operator fun invoke(date: String) = repository.getExpensesByDate(date)
}
