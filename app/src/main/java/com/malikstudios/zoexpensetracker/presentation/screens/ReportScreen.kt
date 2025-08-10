package com.malikstudios.zoexpensetracker.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.malikstudios.zoexpensetracker.R
import com.malikstudios.zoexpensetracker.domain.model.Category
import com.malikstudios.zoexpensetracker.presentation.DocumentItem
import com.malikstudios.zoexpensetracker.presentation.HomeUiEvent
import com.malikstudios.zoexpensetracker.presentation.HomeUiState
import com.malikstudios.zoexpensetracker.presentation.CategoryBreakdown
import com.malikstudios.zoexpensetracker.presentation.DailyTotal
import com.malikstudios.zoexpensetracker.presentation.ReportUiState
import com.malikstudios.zoexpensetracker.ui.theme.AppColors
import com.malikstudios.zoexpensetracker.ui.theme.ZoExpenseTrackerTheme
import com.malikstudios.zoexpensetracker.utils.CurrencyUtils
import com.malikstudios.zoexpensetracker.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    uiState: ReportUiState,
    onEvent: (ReportPeriod) -> Unit,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Report") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { 
                        // Basic export functionality - show a simple message
                        // In a real app, this would generate PDF/CSV and save to storage
                        // For now, we'll just show a placeholder
                    }) {
                        Icon(painter = painterResource(R.drawable.ic_download), contentDescription = "Export")
                    }
                    IconButton(onClick = { 
                        // Basic share functionality - show a simple message
                        // In a real app, this would trigger Share intent with report data
                        // For now, we'll just show a placeholder
                    }) {
                        Icon(Icons.Default.Share, contentDescription = "Share")
                    }
                }
            )
        }
    ) { padding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else if (uiState.error != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_error),
                            contentDescription = "Error",
                            tint = MaterialTheme.colorScheme.onErrorContainer,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Error",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        Text(
                            text = uiState.error,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Period Selector
                item {
                    PeriodSelector(
                        selectedPeriod = uiState.selectedPeriod,
                        onPeriodSelected = { onEvent(it) }
                    )
                }
                
                // Summary Cards
                item {
                    SummaryCards(
                        totalAmount = uiState.totalAmount,
                        totalCount = uiState.totalCount
                    )
                }
                
                // Daily Totals Chart
                item {
                    DailyTotalsChart(dailyTotals = uiState.dailyTotals)
                }
                
                // Category Breakdown
                item {
                    CategoryBreakdown(categoryBreakdown = uiState.categoryBreakdown)
                }
                
                // Recent Expenses
                item {
                    Text(
                        text = "Recent Expenses",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                
                items(uiState.recentExpenses) { expense ->
                    ExpenseItem(expense = expense)
                }
            }
        }
    }
}

@Composable
private fun PeriodSelector(
    selectedPeriod: ReportPeriod,
    onPeriodSelected: (ReportPeriod) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            ReportPeriod.values().forEach { period ->
                FilterChip(
                    selected = selectedPeriod == period,
                    onClick = { onPeriodSelected(period) },
                    label = { Text(period.label) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                    )
                )
            }
        }
    }
}

@Composable
private fun SummaryCards(
    totalAmount: Long,
    totalCount: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCard(
            title = "Total Spent",
            value = CurrencyUtils.formatPaiseToRupeeString(totalAmount),
            icon = painterResource(R.drawable.ic_account_balance_wallet),
            modifier = Modifier.weight(1f)
        )
        SummaryCard(
            title = "Total Count",
            value = totalCount.toString(),
            icon = painterResource(R.drawable.ic_receipt),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun SummaryCard(
    title: String,
    value: String,
    icon: Painter,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = icon,
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DailyTotalsChart(dailyTotals: List<DailyTotal>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Daily Totals (Last 7 Days)",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // Mock chart - simple bar representation
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                if (dailyTotals.isNotEmpty()) {
                    dailyTotals.forEachIndexed { index, dailyTotal ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height((dailyTotal.amount / 10).coerceAtLeast(20).toInt().dp)
                                    .background(
                                        color = AppColors.Amber,
                                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "D${index + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    // Show placeholder bars when no data
                    repeat(7) { day ->
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(20.dp)
                                    .height(50.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceVariant,
                                        shape = RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp)
                                    )
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "D${day + 1}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryBreakdown(categoryBreakdown: List<CategoryBreakdown>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Category Breakdown",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            
            // Mock category data
            val totalAmount = categoryBreakdown.sumOf { it.amount }
            
            categoryBreakdown.forEach { item ->
                CategoryRow(
                    category = item.category,
                    percentage = item.percentage,
                    amount = item.amount
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun CategoryRow(
    category: Category,
    percentage: Double,
    amount: Long
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Category Icon
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = category.name.first().toString(),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        
        Spacer(modifier = Modifier.width(12.dp))
        
        // Category Name
        Text(
            text = category.name,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.weight(1f)
        )
        
        // Percentage
        Text(
            text = "${percentage.toInt()}%",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.primary
        )
        
        Spacer(modifier = Modifier.width(8.dp))
        
        // Amount
        Text(
            text = CurrencyUtils.formatPaiseToRupeeString(amount),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ExpenseItem(expense: DocumentItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = expense.category.name.first().toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            
            Spacer(modifier = Modifier.width(12.dp))
            
            // Expense Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = expense.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = expense.subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            // Amount
            Text(
                text = expense.amount,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = AppColors.Amber
            )
        }
    }
}

enum class ReportPeriod(val label: String) {
    LAST_7_DAYS("7 Days"),
    LAST_30_DAYS("30 Days"),
    LAST_3_MONTHS("3 Months"),
    THIS_YEAR("This Year")
}

@Preview
@Composable
private fun ReportScreenPreview() {
    ZoExpenseTrackerTheme {
        ReportScreen(
            uiState = ReportUiState(
                selectedPeriod = ReportPeriod.LAST_7_DAYS,
                totalAmount = 850L,
                totalCount = 3,
                dailyTotals = listOf(
                    DailyTotal(DateUtils.getLastNDaysFormatted(7)[0], 120L),
                    DailyTotal(DateUtils.getLastNDaysFormatted(7)[1], 80L),
                    DailyTotal(DateUtils.getLastNDaysFormatted(7)[2], 150L),
                    DailyTotal(DateUtils.getLastNDaysFormatted(7)[3], 90L),
                    DailyTotal(DateUtils.getLastNDaysFormatted(7)[4], 110L),
                    DailyTotal(DateUtils.getLastNDaysFormatted(7)[5], 70L),
                    DailyTotal(DateUtils.getLastNDaysFormatted(7)[6], 130L)
                ),
                categoryBreakdown = listOf(
                    CategoryBreakdown(Category.Food, 45.0, 360L),
                    CategoryBreakdown(Category.Travel, 25.0, 200L),
                    CategoryBreakdown(Category.Utility, 20.0, 160L),
                    CategoryBreakdown(Category.Staff, 10.0, 80L)
                ),
                recentExpenses = listOf(
                    DocumentItem("1", "Lunch", "Food • ₹150", "₹150", Category.Food, System.currentTimeMillis()),
                    DocumentItem("2", "Uber Ride", "Travel • ₹200", "₹200", Category.Travel, System.currentTimeMillis()),
                )
            ),
            onEvent = {},
            onNavigateBack = {}
        )
    }
}
