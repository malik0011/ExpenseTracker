package com.malikstudios.zoexpensetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malikstudios.zoexpensetracker.domain.model.Expense
import com.malikstudios.zoexpensetracker.domain.usecase.AddExpenseUseCase
import com.malikstudios.zoexpensetracker.domain.usecase.GetExpensesByDateUseCase
import com.malikstudios.zoexpensetracker.domain.usecase.GetTotalForDateUseCase
import com.malikstudios.zoexpensetracker.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
 * ExpenseViewModel is a ViewModel that manages the state of expenses for a specific date.
 * It provides functionality to add new expenses and retrieve today's expenses and total.
 * It uses the AddExpenseUseCase, GetExpensesByDateUseCase, and GetTotalForDateUseCase
 */
@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val addExpense: AddExpenseUseCase,
    private val getExpensesByDate: GetExpensesByDateUseCase,
    private val getTotalForDate: GetTotalForDateUseCase
) : ViewModel() {

    val todayExpenses = getExpensesByDate(DateUtils.todayDateString())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val todayTotal = getTotalForDate(DateUtils.todayDateString())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    fun addNewExpense(expense: Expense) {
        viewModelScope.launch {
            addExpense(expense)
        }
    }
}