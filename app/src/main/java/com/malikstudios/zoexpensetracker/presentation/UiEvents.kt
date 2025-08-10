package com.malikstudios.zoexpensetracker.presentation

import com.malikstudios.zoexpensetracker.domain.model.Category

// ---------- Home UI Events ----------
sealed class HomeUiEvent {
    data object CreateNew : HomeUiEvent()
    data object Search : HomeUiEvent()
    data object RefreshData : HomeUiEvent()
    data object OpenReport : HomeUiEvent()
    data class OpenItem(val id: String) : HomeUiEvent()
    data class ChangeDate(val date: String) : HomeUiEvent()
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