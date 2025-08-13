package com.malikstudios.zoexpensetracker.presentation

import com.malikstudios.zoexpensetracker.domain.model.Category
import com.malikstudios.zoexpensetracker.presentation.screens.ReportPeriod

import com.malikstudios.zoexpensetracker.utils.DateUtils

// ---------- Home UI State ----------
data class HomeUiState(
    @Deprecated("not required right now use AllItems instead")
    val recentItems: List<DocumentItem> = emptyList(),
    val allItems: List<DocumentItem> = emptyList(),
    val totalSpentToday: Long = 0L, // In paise (smallest Rupee unit)
    val totalCount: Int = 0,
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

// ---------- Report UI State ----------
data class ReportUiState(
    val selectedPeriod: ReportPeriod = ReportPeriod.LAST_7_DAYS,
    val totalAmount: Long = 0L,
    val totalCount: Int = 0,
    val categoryBreakdown: List<CategoryBreakdown> = emptyList(),
    val dailyTotals: List<DailyTotal> = emptyList(),
    val recentExpenses: List<DocumentItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isGeneratingPdf: Boolean = false,
    val pdfMessage: String? = null
)

// ---------- Supporting Data Classes ----------
data class DailyTotal(
    val date: String,
    val amount: Long
)

data class CategoryBreakdown(
    val category: Category,
    val percentage: Double,
    val amount: Long
)