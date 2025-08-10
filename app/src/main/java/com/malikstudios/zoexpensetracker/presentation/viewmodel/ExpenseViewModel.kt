package com.malikstudios.zoexpensetracker.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malikstudios.zoexpensetracker.domain.model.Expense
import com.malikstudios.zoexpensetracker.domain.usecase.AddExpenseUseCase
import com.malikstudios.zoexpensetracker.domain.usecase.GetExpensesByDateUseCase
import com.malikstudios.zoexpensetracker.domain.usecase.GetTotalForDateUseCase
import com.malikstudios.zoexpensetracker.presentation.DocumentItem
import com.malikstudios.zoexpensetracker.presentation.HomeUiEvent
import com.malikstudios.zoexpensetracker.presentation.HomeUiState
import com.malikstudios.zoexpensetracker.utils.CurrencyUtils
import com.malikstudios.zoexpensetracker.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/*
 * ExpenseViewModel manages the UI state for today's expenses.
 * It aggregates expenses, total, and loading/error states into a single UiState
 * so the UI can be completely stateless (UDF pattern).
 */
@HiltViewModel
class ExpenseViewModel @Inject constructor(
    private val addExpense: AddExpenseUseCase,
    private val getExpensesByDate: GetExpensesByDateUseCase,
    private val getTotalForDate: GetTotalForDateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState(
        recentItems = emptyList(),
        allItems = emptyList(),
        totalSpentToday = 0L,
        totalCount = 0,
        isLoading = true
    ))
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var currentDate = DateUtils.todayDateString()

    init {
        observeTodayExpenses()
    }

    private fun observeTodayExpenses() {
        loadExpensesForDate(currentDate)
    }

    private fun loadExpensesForDate(date: String) {
        currentDate = date
        
        combine(
            getExpensesByDate(date),
            getTotalForDate(date)
        ) { expenses, total ->
            val documentItems = expenses.map {
                DocumentItem(
                    id = it.id.toString(),
                    title = it.title,
                    subtitle = "${it.category.name} • ${CurrencyUtils.formatPaiseToRupeeString(it.amountInSmallestUnit)}",
                    amount = CurrencyUtils.formatPaiseToRupeeString(it.amountInSmallestUnit),
                    category = it.category,
                    timestamp = it.timestampMillis,
                    thumbnailRes = null
                )
            }

            // Example: first 5 items are "recent"
            val recent = documentItems.take(5)

            _uiState.value.copy(
                recentItems = recent,
                allItems = documentItems,
                totalSpentToday = total.sumOf { it.amountInSmallestUnit } ,
                totalCount = expenses.size,
                isLoading = false
            )
        }
            .onStart { _uiState.update { it.copy(isLoading = true) } }
            .onEach { newState -> _uiState.value = newState }
            .catch { e ->
                _uiState.update { it.copy(isLoading = false) }
                // In real scenario: handle error logging or add error field to state
            }
            .launchIn(viewModelScope)
    }

    fun onEvent(event: HomeUiEvent) {
        when (event) {
            is HomeUiEvent.OpenItem -> {
                // Navigate to detail screen or open expense
            }
            is HomeUiEvent.CreateNew -> {
                // You might trigger navigation to AddExpense screen
            }
            // Layout toggle removed as it's no longer needed
            is HomeUiEvent.Search -> {
                // Trigger search UI
            }
            is HomeUiEvent.ChangeDate -> {
                loadExpensesForDate(event.date)
            }
            is HomeUiEvent.RefreshData -> {
                loadExpensesForDate(currentDate)
            }
            else -> {}
        }
    }
}