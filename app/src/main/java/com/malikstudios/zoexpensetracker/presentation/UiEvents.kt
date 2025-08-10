package com.malikstudios.zoexpensetracker.presentation

import com.malikstudios.zoexpensetracker.domain.model.Category

// ---------- Home UI Events ----------
sealed interface HomeUiEvent {
    data class OpenItem(val id: String) : HomeUiEvent
    data object CreateNew : HomeUiEvent
    data object ToggleLayout : HomeUiEvent
    data object Search : HomeUiEvent
    data object RefreshData : HomeUiEvent
}

// ---------- Add Expense UI Events ----------
sealed interface AddExpenseUiEvent {
    data class NameChanged(val value: String) : AddExpenseUiEvent
    data class AmountChanged(val value: String) : AddExpenseUiEvent
    data class CategoryChanged(val value: Category) : AddExpenseUiEvent
    data class NotesChanged(val value: String) : AddExpenseUiEvent
    data class DateChanged(val value: String) : AddExpenseUiEvent
    data object ToggleCategoryPicker : AddExpenseUiEvent
    data object Save : AddExpenseUiEvent
    data object Cancel : AddExpenseUiEvent
}

// ---------- Expense List UI Events ----------
sealed interface ExpenseListUiEvent {
    data class OpenExpense(val id: String) : ExpenseListUiEvent
    data class DeleteExpense(val id: String) : ExpenseListUiEvent
    data class ChangeDate(val date: String) : ExpenseListUiEvent
    data class ChangeGrouping(val groupBy: GroupingType) : ExpenseListUiEvent
    data class ChangeSorting(val sortBy: SortingType) : ExpenseListUiEvent
    data object ShowDatePicker : ExpenseListUiEvent
    data object RefreshData : ExpenseListUiEvent
}

// ---------- Report UI Events ----------
sealed interface ReportUiEvent {
    data class ChangeDateRange(val range: DateRange) : ReportUiEvent
    data class ChangeChartType(val type: ChartType) : ReportUiEvent
    data class SelectCustomDateRange(val startDate: String, val endDate: String) : ReportUiEvent
    data object ExportToPdf : ReportUiEvent
    data object ExportToCsv : ReportUiEvent
    data object ShareReport : ReportUiEvent
    data object RefreshData : ReportUiEvent
}