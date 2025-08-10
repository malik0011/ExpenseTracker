package com.malikstudios.zoexpensetracker.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.malikstudios.zoexpensetracker.domain.model.Category
import com.malikstudios.zoexpensetracker.domain.model.Expense
import com.malikstudios.zoexpensetracker.domain.repository.ExpenseRepository
import com.malikstudios.zoexpensetracker.presentation.DocumentItem
import com.malikstudios.zoexpensetracker.presentation.screens.ReportPeriod
import com.malikstudios.zoexpensetracker.presentation.ReportUiState
import com.malikstudios.zoexpensetracker.presentation.CategoryBreakdown
import com.malikstudios.zoexpensetracker.presentation.DailyTotal
import com.malikstudios.zoexpensetracker.utils.DateUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.collections.sumOf

@HiltViewModel
class ReportViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    init {
        loadReportData(ReportPeriod.LAST_7_DAYS)
    }

    fun onPeriodChanged(period: ReportPeriod) {
        loadReportData(period)
    }

    private fun loadReportData(period: ReportPeriod) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            
            try {
                // Calculate date range based on period
                val (startDate, endDate) = calculateDateRange(period)
                
                // Load expenses for the period
                val expenses = expenseRepository.getExpensesByDateRange(startDate, endDate).first()
                
                // Calculate totals and breakdowns
                val totalAmount = expenses.sumOf { it.amountInSmallestUnit }
                val totalCount = expenses.size
                val categoryBreakdown = calculateCategoryBreakdown(expenses)
                val dailyTotals = calculateDailyTotals(expenses, startDate, endDate)
                
                _uiState.value = _uiState.value.copy(
                    selectedPeriod = period,
                    totalAmount = totalAmount,
                    totalCount = totalCount,
                    categoryBreakdown = categoryBreakdown,
                    dailyTotals = dailyTotals,
                    recentExpenses = expenses.take(5).map { expense ->
                        DocumentItem(
                            id = expense.id.toString(),
                            title = expense.title,
                            subtitle = "${expense.category.name} • ${formatAmount(expense.amountInSmallestUnit)}",
                            amount = formatAmount(expense.amountInSmallestUnit),
                            category = expense.category,
                            timestamp = expense.timestampMillis
                        )
                    },
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Failed to load report data"
                )
            }
        }
    }

//    private suspend fun getTodayTotal(listFlow: Flow<List<Expense>>): Long {
//        return listFlow
//            .map { expenses -> expenses.sumOf { it.amountInSmallestUnit } }
//            .first() // get the first emitted value
//    }


    private fun calculateDateRange(period: ReportPeriod): Pair<String, String> {
        val calendar = Calendar.getInstance()
        val endDate = DateUtils.todayDateString()
        
        when (period) {
            ReportPeriod.LAST_7_DAYS -> calendar.add(Calendar.DAY_OF_YEAR, -7)
            ReportPeriod.LAST_30_DAYS -> calendar.add(Calendar.DAY_OF_YEAR, -30)
            ReportPeriod.LAST_3_MONTHS -> calendar.add(Calendar.MONTH, -3)
            ReportPeriod.THIS_YEAR -> {
                calendar.set(Calendar.DAY_OF_YEAR, 1)
                calendar.set(Calendar.MONTH, 0)
            }
        }
        
        val startDate = DateUtils.fromMillisToDateString(calendar.timeInMillis)
        return startDate to endDate
    }

    private fun calculateCategoryBreakdown(expenses: List<Expense>): List<CategoryBreakdown> {
        val categoryMap = mutableMapOf<Category, Long>()
        
        expenses.forEach { expense ->
            categoryMap[expense.category] = categoryMap.getOrDefault(expense.category, 0L) + expense.amountInSmallestUnit
        }
        
        val total = categoryMap.values.sum()
        
        return categoryMap.map { (category, amount) ->
            CategoryBreakdown(
                category = category,
                amount = amount,
                percentage = if (total > 0) (amount.toDouble() / total * 100) else 0.0
            )
        }.sortedByDescending { it.amount }
    }

    private fun calculateDailyTotals(expenses: List<Expense>, startDate: String, endDate: String): List<DailyTotal> {
        val dailyMap = mutableMapOf<String, Long>()
        
        expenses.forEach { expense ->
            val date = DateUtils.fromMillisToDateString(expense.timestampMillis)
            dailyMap[date] = dailyMap.getOrDefault(date, 0L) + expense.amountInSmallestUnit
        }
        
        return dailyMap.map { (date, amount) ->
            DailyTotal(date = date, amount = amount)
        }.sortedBy { it.date }
    }

    private fun formatAmount(amountInPaise: Long): String {
        return "₹${amountInPaise / 100}.${String.format("%02d", amountInPaise % 100)}"
    }
}
