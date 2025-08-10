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
import com.malikstudios.zoexpensetracker.utils.PdfGenerator
import com.malikstudios.zoexpensetracker.utils.CsvGenerator
import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import java.io.File
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import android.os.Environment

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
    
    fun exportToPdf(context: Context) {
        Log.d("ReportViewModel", "Starting PDF export...")
        viewModelScope.launch(Dispatchers.IO) {
            try {
                Log.d("ReportViewModel", "Setting loading state...")
                _uiState.value = _uiState.value.copy(isGeneratingPdf = true, pdfMessage = null)
                
                val currentState = _uiState.value
                Log.d("ReportViewModel", "Generating PDF with data: period=${currentState.selectedPeriod}, total=${currentState.totalAmount}, categories=${currentState.categoryBreakdown.size}, daily=${currentState.dailyTotals.size}")
                
                // Check if we have data to generate PDF
                if (currentState.totalAmount == 0L && currentState.categoryBreakdown.isEmpty()) {
                    Log.w("ReportViewModel", "No data available for PDF generation")
                    withContext(Dispatchers.Main) {
                        _uiState.value = _uiState.value.copy(
                            isGeneratingPdf = false,
                            pdfMessage = "No data available to generate PDF"
                        )
                    }
                    return@launch
                }
                
                val pdfFile = PdfGenerator().generateExpenseReport(
                    context = context,
                    period = currentState.selectedPeriod,
                    totalAmount = currentState.totalAmount,
                    totalCount = currentState.totalCount,
                    categoryBreakdown = currentState.categoryBreakdown,
                    dailyTotals = currentState.dailyTotals,
                    recentExpenses = currentState.recentExpenses
                )
                
                Log.d("ReportViewModel", "PDF generation result: ${pdfFile?.absolutePath}")
                
                withContext(Dispatchers.Main) {
                    if (pdfFile != null) {
                        // Copy to Downloads folder for user access
                        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                        val downloadsFile = File(downloadsDir, pdfFile.name)
                        
                        Log.d("ReportViewModel", "Copying to Downloads: ${downloadsFile.absolutePath}")
                        
                        try {
                            pdfFile.copyTo(downloadsFile, overwrite = true)
                            Log.d("ReportViewModel", "PDF saved to Downloads: ${downloadsFile.absolutePath}")
                            
                            _uiState.value = _uiState.value.copy(
                                isGeneratingPdf = false,
                                pdfMessage = "PDF saved to Downloads: ${downloadsFile.name}"
                            )
                        } catch (e: Exception) {
                            Log.e("ReportViewModel", "Error copying to Downloads", e)
                            _uiState.value = _uiState.value.copy(
                                isGeneratingPdf = false,
                                pdfMessage = "Error saving PDF: ${e.message}"
                            )
                        }
                    } else {
                        Log.e("ReportViewModel", "PDF generation returned null")
                        _uiState.value = _uiState.value.copy(
                            isGeneratingPdf = false,
                            pdfMessage = "Failed to generate PDF"
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("ReportViewModel", "Error in exportToPdf", e)
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isGeneratingPdf = false,
                        pdfMessage = "Error: ${e.message}"
                    )
                }
            }
        }
    }

    fun exportToCsv(context: Context): File? {
        return try {
            val currentState = _uiState.value
            val file = CsvGenerator().generateExpenseReport(
                context = context,
                period = currentState.selectedPeriod,
                totalAmount = currentState.totalAmount,
                totalCount = currentState.totalCount,
                categoryBreakdown = currentState.categoryBreakdown,
                dailyTotals = currentState.dailyTotals,
                recentExpenses = currentState.recentExpenses
            )
            Log.d("ReportViewModel", "CSV saved to: ${file.absolutePath}")
            file
        } catch (e: Exception) {
            Log.e("ReportViewModel", "Error generating CSV", e)
            null
        }
    }

    fun shareReport(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _uiState.value = _uiState.value.copy(isGeneratingPdf = true, pdfMessage = null)
                
                val currentState = _uiState.value
                val pdfFile = PdfGenerator().generateExpenseReport(
                    context = context,
                    period = currentState.selectedPeriod,
                    totalAmount = currentState.totalAmount,
                    totalCount = currentState.totalCount,
                    categoryBreakdown = currentState.categoryBreakdown,
                    dailyTotals = currentState.dailyTotals,
                    recentExpenses = currentState.recentExpenses
                )
                
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(isGeneratingPdf = false)
                    
                    if (pdfFile != null) {
                        try {
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.fileprovider",
                                pdfFile
                            )
                            
                            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                type = "application/pdf"
                                putExtra(Intent.EXTRA_STREAM, uri)
                                putExtra(Intent.EXTRA_SUBJECT, "Expense Report - ${currentState.selectedPeriod.label}")
                                putExtra(Intent.EXTRA_TEXT, "Here's your expense report for ${currentState.selectedPeriod.label}")
                                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                            }
                            
                            context.startActivity(Intent.createChooser(shareIntent, "Share Report"))
                        } catch (e: Exception) {
                            Log.e("ReportViewModel", "Error sharing report", e)
                            _uiState.value = _uiState.value.copy(
                                pdfMessage = "Error sharing: ${e.message}"
                            )
                        }
                    } else {
                        _uiState.value = _uiState.value.copy(
                            pdfMessage = "Failed to generate PDF for sharing"
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _uiState.value = _uiState.value.copy(
                        isGeneratingPdf = false,
                        pdfMessage = "Error: ${e.message}"
                    )
                }
                Log.e("ReportViewModel", "Error in shareReport", e)
            }
        }
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
