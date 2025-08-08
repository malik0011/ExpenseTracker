package com.malikstudios.zoexpensetracker.presentation.navigation


import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.malikstudios.zoexpensetracker.presentation.screens.AddExpenseScreen
import com.malikstudios.zoexpensetracker.presentation.viewmodel.ExpenseViewModel
import com.malikstudios.zoexpensetracker.presentation.screens.HomeScreen
import com.malikstudios.zoexpensetracker.presentation.screens.TransactionHistoryScreen

@Composable
fun ExpenseNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            val viewModel: ExpenseViewModel = hiltViewModel()
            HomeScreen()
        }
        composable(NavRoutes.AddExpense.route) {
            val viewModel: ExpenseViewModel = hiltViewModel()
            AddExpenseScreen()
        }
        composable(NavRoutes.History.route) {
            val viewModel: ExpenseViewModel = hiltViewModel()
            TransactionHistoryScreen()
        }
    }
}