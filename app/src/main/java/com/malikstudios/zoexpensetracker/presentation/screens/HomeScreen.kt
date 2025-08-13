package com.malikstudios.zoexpensetracker.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.malikstudios.zoexpensetracker.R
import com.malikstudios.zoexpensetracker.domain.model.Category
import com.malikstudios.zoexpensetracker.presentation.DocumentItem
import com.malikstudios.zoexpensetracker.presentation.HomeUiEvent
import com.malikstudios.zoexpensetracker.presentation.HomeUiState
import com.malikstudios.zoexpensetracker.ui.theme.AppColors
import com.malikstudios.zoexpensetracker.ui.theme.ZoExpenseTrackerTheme
import com.malikstudios.zoexpensetracker.utils.CurrencyUtils
import com.malikstudios.zoexpensetracker.utils.DateUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    onNavigateToReport: () -> Unit = {}
) {
    var selectedDate by remember { mutableStateOf(DateUtils.todayDateString()) }
    var groupByCategory by remember { mutableStateOf(false) }
    var showDatePicker by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Expense Tracker") },
                actions = {
                    IconButton(onClick = { onEvent(HomeUiEvent.RefreshData) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = { onEvent(HomeUiEvent.OpenReport) }) {
                        Icon(painter = painterResource(R.drawable.ic_bar_chart), contentDescription = "Report")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(HomeUiEvent.CreateNew) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Expense")
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header Section with Stats
            HeaderSection(
                totalAmount = uiState.totalSpentToday,
                totalCount = uiState.totalCount,
                selectedDate = selectedDate,
                onDateClick = { showDatePicker = true },
                onTodayClick = {
                    selectedDate = DateUtils.todayDateString()
                    onEvent(HomeUiEvent.ChangeDate(DateUtils.todayDateString()))
                }
            )

            // Controls Section
            ControlsSection(
                groupByCategory = groupByCategory,
                onGroupingChanged = { groupByCategory = it }
            )

            // Error Message
            if (uiState.error != null) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = uiState.error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            // Expenses List
            ExpensesList(
                expenses = uiState.allItems,
                groupByCategory = groupByCategory,
                onExpenseClick = { expenseId -> onEvent(HomeUiEvent.OpenItem(expenseId)) },
                isLoading = uiState.isLoading,
                selectedDate = selectedDate
            )
        }

        // Date Picker Dialog
        if (showDatePicker) {
            FullScreenDatePicker(
                onDateSelected = { date ->
                    selectedDate = date
                    onEvent(HomeUiEvent.ChangeDate(date))
                    showDatePicker = false
                },
                onDismiss = { showDatePicker = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FullScreenDatePicker(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )

    Surface(
        modifier = Modifier.fillMaxSize().zIndex(10f),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header
            Spacer(Modifier.height(48.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Select Date",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close"
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Date Picker
            DatePicker(
                state = datePickerState,
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(millis))
                            onDateSelected(date)
                        }
                    }
                ) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
private fun HeaderSection(
    totalAmount: Long,
    totalCount: Int,
    selectedDate: String,
    onDateClick: () -> Unit,
    onTodayClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Date Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit_calender),
                    contentDescription = "Date",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.clickable { onDateClick() }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (selectedDate == DateUtils.todayDateString()) "Today" else selectedDate,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.clickable { onDateClick() }
                )
                
                if (selectedDate != DateUtils.todayDateString()) {
                    Spacer(modifier = Modifier.width(12.dp))
                    Surface(
                        modifier = Modifier.clickable { onTodayClick() },
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = "Today",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Stats Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Total Amount
                Column(
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Total Spent",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        text = CurrencyUtils.formatPaiseToRupeeString(totalAmount),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
                
                // Total Count
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Total Items",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                    )
                    Text(
                        text = totalCount.toString(),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun ControlsSection(
    groupByCategory: Boolean,
    onGroupingChanged: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Group by:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Row {
            // Time Grouping
            Surface(
                modifier = Modifier.clickable { onGroupingChanged(false) },
                shape = RoundedCornerShape(20.dp),
                color = if (!groupByCategory) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_schedule),
                        contentDescription = "Time",
                        modifier = Modifier.size(16.dp),
                        tint = if (!groupByCategory) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Time",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (!groupByCategory) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Category Grouping
            Surface(
                modifier = Modifier.clickable { onGroupingChanged(true) },
                shape = RoundedCornerShape(20.dp),
                color = if (groupByCategory) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_category),
                        contentDescription = "Category",
                        modifier = Modifier.size(16.dp),
                        tint = if (groupByCategory) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Category",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (groupByCategory) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ExpensesList(
    expenses: List<DocumentItem>,
    groupByCategory: Boolean,
    onExpenseClick: (String) -> Unit,
    isLoading: Boolean = false,
    selectedDate: String
) {
    if (isLoading && expenses.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Loading expenses...",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    } else if (expenses.isEmpty()) {
        EmptyState(selectedDate = selectedDate)
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (groupByCategory) {
                // Group by category

                //put this on derived state of
                val grpItems by derivedStateOf {
                    expenses.groupBy { it.category }
                }
                val groupedExpenses = expenses.groupBy { it.category }

                groupedExpenses.forEach { (category, categoryExpenses) ->
                    item {
                        CategoryHeader(category, categoryExpenses.sumOf { 
                            it.amount.replace("₹", "").toDoubleOrNull() ?: 0.0 
                        })
                    }
                    items(categoryExpenses) { expense ->
                        ExpenseItem(expense, onExpenseClick)
                    }
                    item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            } else {
                // Group by time (chronological)
                items(expenses.sortedByDescending { it.timestamp }) { expense ->
                    ExpenseItem(expense, onExpenseClick)
                }
            }
        }
    }
}

@Composable
private fun CategoryHeader(category: Category, totalAmount: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = category.name,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "₹${String.format("%.2f", totalAmount)}",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = AppColors.Success
        )
    }
}

@Composable
private fun ExpenseItem(expense: DocumentItem, onExpenseClick: (String) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExpenseClick(expense.id) },
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
                    .background(AppColors.Info.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = expense.category.name.first().toString(),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary
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

@Composable
private fun EmptyState(selectedDate: String = DateUtils.todayDateString()) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_category),
                contentDescription = "No Expenses",
                modifier = Modifier.size(64.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = if (selectedDate == DateUtils.todayDateString()) "No expenses yet" else "No expenses for this date",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (selectedDate == DateUtils.todayDateString()) 
                    "Add your first expense to get started" 
                else 
                    "Try selecting a different date or add an expense for today",
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview
@Composable
private fun HomePreview() {
    ZoExpenseTrackerTheme {
        HomeScreen(
            uiState = HomeUiState(
                recentItems = listOf(
                    DocumentItem("1", "Lunch", "Food • ₹150", "₹150", Category.Food, System.currentTimeMillis()),
                    DocumentItem("2", "Uber Ride", "Travel • ₹200", "₹200", Category.Travel, System.currentTimeMillis()),
                ),
                allItems = listOf(
                    DocumentItem("1", "Lunch", "Food • ₹150", "₹150", Category.Food, System.currentTimeMillis()),
                    DocumentItem("2", "Uber Ride", "Travel • ₹200", "₹200", Category.Travel, System.currentTimeMillis()),
                    DocumentItem("3", "Internet Bill", "Utility • ₹500", "₹500", Category.Utility, System.currentTimeMillis()),
                ),
                totalSpentToday = 850L,
                totalCount = 3,
                isLoading = false
            ),
            onEvent = {},
            onNavigateToReport = {}
        )
    }
}
