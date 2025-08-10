package com.malikstudios.zoexpensetracker.presentation.navigation


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.malikstudios.zoexpensetracker.presentation.HomeUiEvent
import com.malikstudios.zoexpensetracker.presentation.screens.AddExpenseScreen
import com.malikstudios.zoexpensetracker.presentation.screens.HomeScreen
import com.malikstudios.zoexpensetracker.presentation.screens.TransactionHistoryScreen
import com.malikstudios.zoexpensetracker.presentation.viewmodel.AddExpenseViewModel
import com.malikstudios.zoexpensetracker.presentation.viewmodel.ExpenseViewModel
import com.malikstudios.zoexpensetracker.ui.theme.ZoExpenseTrackerTheme

@Composable
fun ExpenseNavGraph(
    innerPadding: PaddingValues,
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route
    ) {
        composable(NavRoutes.Home.route) {
            val viewModel: ExpenseViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsState()

            HomeScreen(
                innerPadding = innerPadding,
                uiState = state,
                onEvent = { event ->
                    if (event == HomeUiEvent.CreateNew) {
                        navController.navigate(NavRoutes.AddExpense.route)
                        return@HomeScreen
                    }
                    viewModel.onEvent(event)
                }
            )
        }

        composable(NavRoutes.AddExpense.route) {
            val viewModel: AddExpenseViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            AddExpenseScreen(
                uiState = uiState,
                onEvent = { viewModel.onEvent(it) },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(NavRoutes.History.route) {
            val viewModel: ExpenseViewModel = hiltViewModel()
            ZoExpenseTrackerTheme {
                TransactionHistoryScreen()
            }
        }
    }
}