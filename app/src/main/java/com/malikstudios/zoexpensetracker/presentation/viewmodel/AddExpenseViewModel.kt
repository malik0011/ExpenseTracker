package com.malikstudios.zoexpensetracker.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malikstudios.zoexpensetracker.domain.model.Category
import com.malikstudios.zoexpensetracker.domain.model.Expense

import com.malikstudios.zoexpensetracker.domain.usecase.AddExpenseUseCase
import com.malikstudios.zoexpensetracker.domain.usecase.GetExpensesByDateUseCase
import com.malikstudios.zoexpensetracker.domain.usecase.GetTotalForDateUseCase
import com.malikstudios.zoexpensetracker.presentation.AddExpenseUiEvent
import com.malikstudios.zoexpensetracker.presentation.AddExpenseUiState
import com.malikstudios.zoexpensetracker.utils.CurrencyUtils
import com.malikstudios.zoexpensetracker.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val addExpense: AddExpenseUseCase,
    private val getTotalForDate: GetTotalForDateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddExpenseUiState())
    val uiState: StateFlow<AddExpenseUiState> = _uiState

    init {
        getTodayTotal()
    }

    fun onEvent(event: AddExpenseUiEvent) {
        when (event) {
            is AddExpenseUiEvent.NameChanged -> {
                _uiState.value = _uiState.value.copy(name = event.value, error = null)
            }
            is AddExpenseUiEvent.AmountChanged -> {
                _uiState.value = _uiState.value.copy(amount = event.value, error = null)
            }
            is AddExpenseUiEvent.CategoryChanged -> {
                _uiState.value = _uiState.value.copy(category = event.value, error = null)
            }
            is AddExpenseUiEvent.NotesChanged -> {
                _uiState.value = _uiState.value.copy(notes = event.value, error = null)
            }
            is AddExpenseUiEvent.DateChanged -> {
                _uiState.value = _uiState.value.copy(date = event.value, error = null)
            }
            is AddExpenseUiEvent.ToggleCategoryPicker -> {
                _uiState.value = _uiState.value.copy(
                    showCategoryPicker = !_uiState.value.showCategoryPicker
                )
            }
            AddExpenseUiEvent.Save -> {
                saveExpense()
            }
            AddExpenseUiEvent.Cancel -> {}
        }
    }

    private fun saveExpense() {
        val state = _uiState.value

        // Input validation
        if (state.name.isBlank() || state.amount.isBlank()) {
            _uiState.value = state.copy(error = "Please fill title and amount")
            return
        }

        // Validate amount is positive
        val amountValue = state.amount.toDoubleOrNull()
        if (amountValue == null || amountValue <= 0) {
            _uiState.value = state.copy(error = "Please enter a valid amount greater than 0")
            return
        }

        viewModelScope.launch {
            try {
                _uiState.value = state.copy(isSaving = true, error = null)

                // Save expense using Rupee system
                val expense = Expense(
                    title = state.name,
                    amountInSmallestUnit = CurrencyUtils.parseToPaise(state.amount).getOrNull() ?: 0L,
                    category = state.category,
                    notes = state.notes,
                    date = state.date,
                    timestampMillis = System.currentTimeMillis()
                )
                
                addExpense(expense)

                // Set success state
                _uiState.value = state.copy(
                    isSaving = false,
                    success = true,
                    name = "",
                    amount = "",
                    notes = "",
                    date = DateUtils.todayDateString(),
                    category = Category.Other,

                )

                getTodayTotal() // Refresh today's total after saving

            } catch (e: Exception) {
                _uiState.value = state.copy(isSaving = false, error = e.message ?: "Unknown error")
            }
        }
    }

    private fun getTodayTotal() {
        Log.d("testAyan", "getTodayTotal: ")
        viewModelScope.launch {
            try {
                var todayTotal = 0L
                getTotalForDate(DateUtils.todayDateString()).collect {
                    it.forEach {
                        todayTotal += it.amountInSmallestUnit
                    }
                    Log.d("testAyan", "getTodayTotal: $todayTotal")
                    _uiState.value = _uiState.value.copy(todayTotal = todayTotal)
                }

            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(error = e.message ?: "Failed to fetch today's total")
            }
        }
    }
}

