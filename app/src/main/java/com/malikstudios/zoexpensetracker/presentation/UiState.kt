package com.malikstudios.zoexpensetracker.presentation

import com.malikstudios.zoexpensetracker.domain.model.Category

import com.malikstudios.zoexpensetracker.utils.DateUtils

// ---------- Home UI State ----------
data class HomeUiState(
    val recentItems: List<DocumentItem> = emptyList(),
    val allItems: List<DocumentItem> = emptyList(),
    val totalSpentToday: Long = 0L, // In paise (smallest Rupee unit)
    val totalCount: Int = 0,
    val isGrid: Boolean = true,
    val isLoading: Boolean = true,
    val error: String? = null
)

// ---------- Document Item Model ----------
// Enhanced document item with better visual information
data class DocumentItem(
    val id: String,
    val title: String,
    val subtitle: String,
    val amount: String, // Formatted amount in Rupees
    val category: Category,
    val timestamp: Long,
    val thumbnailRes: Int? = null
)

// ---------- Add Expense UI State ----------
data class AddExpenseUiState(
    val name: String = "",
    val amount: String = "",
    val category: Category = Category.Other,
    val notes: String = "",
    val date: String = DateUtils.todayDateString(),
    val todayTotal: Long = 0L, // Real-time today's total in paise
    val isSaving: Boolean = false,
    val showCategoryPicker: Boolean = false,
    val error: String? = null,
    val success: Boolean = false
)

// ---------- Expense List UI State ----------
data class ExpenseListUiState(
    val expenses: List<DocumentItem> = emptyList(),
    val selectedDate: String = DateUtils.todayDateString(),
    val totalAmount: Long = 0L,
    val totalCount: Int = 0,
    val groupBy: GroupingType = GroupingType.TIME,
    val sortBy: SortingType = SortingType.NEWEST_FIRST,
    val isLoading: Boolean = false,
    val error: String? = null
)

// ---------- Report UI State ----------
data class ReportUiState(
    val dailyTotals: List<DailyTotal> = emptyList(),
    val categoryTotals: List<CategoryTotal> = emptyList(),
    val totalExpenses: Long = 0L,
    val dateRange: DateRange = DateRange.LAST_7_DAYS,
    val chartType: ChartType = ChartType.BAR,
    val isLoading: Boolean = false,
    val error: String? = null
)

// ---------- Supporting Data Classes ----------
data class DailyTotal(
    val date: String,
    val amount: Long,
    val count: Int
)

data class CategoryTotal(
    val category: Category,
    val amount: Long,
    val count: Int,
    val percentage: Float
)

enum class GroupingType {
    TIME, CATEGORY, AMOUNT
}

enum class SortingType {
    NEWEST_FIRST, OLDEST_FIRST, AMOUNT_HIGH_LOW, AMOUNT_LOW_HIGH, ALPHABETICAL
}

enum class DateRange {
    TODAY, LAST_7_DAYS, LAST_30_DAYS, THIS_MONTH, LAST_MONTH, CUSTOM
}

enum class ChartType {
    BAR, LINE, PIE
}