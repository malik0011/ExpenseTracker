package com.malikstudios.zoexpensetracker.utils

import android.content.Context
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.graphics.pdf.PdfDocument.PageInfo
import androidx.core.content.FileProvider
import com.malikstudios.zoexpensetracker.presentation.CategoryBreakdown
import com.malikstudios.zoexpensetracker.presentation.DailyTotal
import com.malikstudios.zoexpensetracker.presentation.DocumentItem
import com.malikstudios.zoexpensetracker.presentation.screens.ReportPeriod
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PdfGenerator {
    
    fun generateExpenseReport(
        context: Context,
        period: ReportPeriod,
        totalAmount: Long,
        totalCount: Int,
        categoryBreakdown: List<CategoryBreakdown>,
        dailyTotals: List<DailyTotal>,
        recentExpenses: List<DocumentItem>
    ): File? {
        try {
            val pdfDocument = PdfDocument()
            val pageInfo = PageInfo.Builder(595, 842, 1).create() // A4 size
            val page = pdfDocument.startPage(pageInfo)
            val canvas = page.canvas
            
            val paint = Paint().apply {
                color = Color.BLACK
                textSize = 12f
                typeface = android.graphics.Typeface.DEFAULT
            }
            
            val titlePaint = Paint().apply {
                color = Color.BLACK
                textSize = 24f
                textAlign = Paint.Align.CENTER
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }
            
            val headerPaint = Paint().apply {
                color = Color.BLACK
                textSize = 16f
                typeface = android.graphics.Typeface.DEFAULT_BOLD
            }
            
            val subtitlePaint = Paint().apply {
                color = Color.DKGRAY
                textSize = 14f
                typeface = android.graphics.Typeface.DEFAULT
            }
            
            var yPosition = 50f
            val pageWidth = 595f
            val leftMargin = 50f
            
            // Title
            canvas.drawText("Expense Report", pageWidth / 2, yPosition, titlePaint)
            yPosition += 40f
            
            // Period and Date
            canvas.drawText("Period: ${period.label}", leftMargin, yPosition, subtitlePaint)
            yPosition += 25f
            canvas.drawText("Generated: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}", leftMargin, yPosition, subtitlePaint)
            yPosition += 40f
            
            // Summary
            canvas.drawText("Summary", leftMargin, yPosition, headerPaint)
            yPosition += 25f
            canvas.drawText("Total Spent: ${CurrencyUtils.formatPaiseToRupeeString(totalAmount)}", leftMargin, yPosition, paint)
            yPosition += 20f
            canvas.drawText("Total Count: $totalCount", leftMargin, yPosition, paint)
            yPosition += 40f
            
            // Category Breakdown
            if (categoryBreakdown.isNotEmpty()) {
                canvas.drawText("Category Breakdown", leftMargin, yPosition, headerPaint)
                yPosition += 25f
                
                categoryBreakdown.forEach { category ->
                    val text = "${category.category.name}: ${CurrencyUtils.formatPaiseToRupeeString(category.amount)} (${String.format("%.1f", category.percentage)}%)"
                    canvas.drawText(text, leftMargin + 20f, yPosition, paint)
                    yPosition += 20f
                }
                yPosition += 20f
            }
            
            // Daily Totals (simplified)
            if (dailyTotals.isNotEmpty()) {
                canvas.drawText("Daily Totals", leftMargin, yPosition, headerPaint)
                yPosition += 25f
                
                dailyTotals.take(10).forEach { daily -> // Limit to 10 to avoid overflow
                    val text = "${daily.date}: ${CurrencyUtils.formatPaiseToRupeeString(daily.amount)}"
                    canvas.drawText(text, leftMargin + 20f, yPosition, paint)
                    yPosition += 20f
                }
            }
            
            // Recent Expenses (simplified)
            if (recentExpenses.isNotEmpty()) {
                yPosition += 20f
                canvas.drawText("Recent Expenses", leftMargin, yPosition, headerPaint)
                yPosition += 25f
                
                recentExpenses.take(8).forEach { expense -> // Limit to 8 to avoid overflow
                    val text = "${expense.title} - ${expense.subtitle}"
                    canvas.drawText(text, leftMargin + 20f, yPosition, paint)
                    yPosition += 20f
                }
            }
            
            pdfDocument.finishPage(page)
            
            // Create file in app's external files directory for sharing
            val fileName = "expense_report_${period.name.lowercase()}_${System.currentTimeMillis()}.pdf"
            val reportsDir = File(context.getExternalFilesDir(null), "reports")
            if (!reportsDir.exists()) {
                reportsDir.mkdirs()
            }
            val file = File(reportsDir, fileName)
            
            try {
                val fileOutputStream = FileOutputStream(file)
                pdfDocument.writeTo(fileOutputStream)
                pdfDocument.close()
                fileOutputStream.close()
                
                return file
            } catch (e: Exception) {
                pdfDocument.close()
                if (file.exists()) file.delete()
                throw e
            }
        } catch (e: Exception) {
            android.util.Log.e("PdfGenerator", "Error generating PDF", e)
            return null
        }
    }
}
