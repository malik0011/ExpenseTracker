package com.malikstudios.zoexpensetracker.utils

import android.content.Context
import android.os.Environment
import com.malikstudios.zoexpensetracker.presentation.CategoryBreakdown
import com.malikstudios.zoexpensetracker.presentation.DailyTotal
import com.malikstudios.zoexpensetracker.presentation.DocumentItem
import com.malikstudios.zoexpensetracker.presentation.screens.ReportPeriod
import com.malikstudios.zoexpensetracker.utils.CurrencyUtils
import java.io.File
import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.*

class CsvGenerator {
    
    fun generateExpenseReport(
        context: Context,
        period: ReportPeriod,
        totalAmount: Long,
        totalCount: Int,
        categoryBreakdown: List<CategoryBreakdown>,
        dailyTotals: List<DailyTotal>,
        recentExpenses: List<DocumentItem>
    ): File {
        val fileName = "expense_report_${period.name.lowercase()}_${System.currentTimeMillis()}.csv"
        val downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(downloadsDir, fileName)
        val writer = FileWriter(file)
        
        try {
            // Header
            writer.append("Expense Report\n")
            writer.append("Period,${period.label}\n")
            writer.append("Generated,${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}\n")
            writer.append("\n")
            
            // Summary
            writer.append("Summary\n")
            writer.append("Total Spent,${CurrencyUtils.formatPaiseToRupeeString(totalAmount)}\n")
            writer.append("Total Count,$totalCount\n")
            writer.append("\n")
            
            // Category Breakdown
            if (categoryBreakdown.isNotEmpty()) {
                writer.append("Category Breakdown\n")
                writer.append("Category,Amount,Percentage\n")
                categoryBreakdown.forEach { category ->
                    writer.append("${category.category.name},${CurrencyUtils.formatPaiseToRupeeString(category.amount)},${String.format("%.1f", category.percentage)}%\n")
                }
                writer.append("\n")
            }
            
            // Daily Totals
            if (dailyTotals.isNotEmpty()) {
                writer.append("Daily Totals\n")
                writer.append("Date,Amount\n")
                dailyTotals.forEach { daily ->
                    writer.append("${daily.date},${CurrencyUtils.formatPaiseToRupeeString(daily.amount)}\n")
                }
                writer.append("\n")
            }
            
            // Recent Expenses
            if (recentExpenses.isNotEmpty()) {
                writer.append("Recent Expenses\n")
                writer.append("Title,Category,Amount,Date\n")
                recentExpenses.forEach { expense ->
                    val date = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date(expense.timestamp))
                    writer.append("${expense.title},${expense.category.name},${expense.amount},$date\n")
                }
            }
            
        } finally {
            writer.close()
        }
        
        return file
    }
}
