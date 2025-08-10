package com.malikstudios.zoexpensetracker.presentation.navigation


import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import android.content.Intent
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.malikstudios.zoexpensetracker.presentation.HomeUiEvent
import com.malikstudios.zoexpensetracker.presentation.screens.AddExpenseScreen
import com.malikstudios.zoexpensetracker.presentation.screens.HomeScreen
import com.malikstudios.zoexpensetracker.presentation.screens.ReportScreen
import com.malikstudios.zoexpensetracker.presentation.screens.TransactionHistoryScreen
import com.malikstudios.zoexpensetracker.presentation.viewmodel.AddExpenseViewModel
import com.malikstudios.zoexpensetracker.presentation.viewmodel.ExpenseViewModel
import com.malikstudios.zoexpensetracker.presentation.viewmodel.ReportViewModel
import com.malikstudios.zoexpensetracker.ui.theme.ZoExpenseTrackerTheme
import android.util.Log
import android.content.Context
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
                    if (event == HomeUiEvent.OpenReport) {
                        navController.navigate(NavRoutes.Report.route)
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
        
        composable(NavRoutes.Report.route) {
            val viewModel: ReportViewModel = hiltViewModel()
            val state by viewModel.uiState.collectAsState()
            val context = LocalContext.current

            ReportScreen(
                uiState = state,
                onEvent = { viewModel.onPeriodChanged(it) },
                onNavigateBack = { navController.popBackStack() },
                onExportPdf = {
                    viewModel.exportToPdf(context)
                    // Add toast notification for PDF export (same as CSV)
                    android.widget.Toast.makeText(context, "Generating PDF...", android.widget.Toast.LENGTH_SHORT).show()
                },
                onExportCsv = {
                    val file = viewModel.exportToCsv(context)
                    if (file != null) {
                        android.widget.Toast.makeText(context, "CSV saved to: ${file.absolutePath}", android.widget.Toast.LENGTH_LONG).show()
                    } else {
                        android.widget.Toast.makeText(context, "Failed to generate CSV", android.widget.Toast.LENGTH_SHORT).show()
                    }
                },
                onShareReport = {
                    viewModel.shareReport(context)
                }
            )
        }
    }
}