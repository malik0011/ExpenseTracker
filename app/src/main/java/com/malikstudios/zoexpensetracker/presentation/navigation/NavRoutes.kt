package com.malikstudios.zoexpensetracker.presentation.navigation

sealed class NavRoutes(val route: String) {
    data object Home : NavRoutes("home")
    data object AddExpense : NavRoutes("add_expense")
    data object History : NavRoutes("history")
}