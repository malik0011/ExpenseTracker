package com.malikstudios.zoexpensetracker.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.malikstudios.zoexpensetracker.domain.model.Category

import com.malikstudios.zoexpensetracker.presentation.AddExpenseUiEvent
import com.malikstudios.zoexpensetracker.presentation.AddExpenseUiState
import com.malikstudios.zoexpensetracker.ui.theme.AppColors
import com.malikstudios.zoexpensetracker.ui.theme.ZoExpenseTrackerTheme
import com.malikstudios.zoexpensetracker.utils.CurrencyUtils
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    uiState: AddExpenseUiState,
    onEvent: (AddExpenseUiEvent) -> Unit,
    onNavigateBack: () -> Unit = {}
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val conScope = rememberCoroutineScope()
    
    // Handle success state
    LaunchedEffect(uiState.success) {
        if (uiState.success) {
            conScope.launch {
                // Reset the state after showing success message
                snackbarHostState.showSnackbar("Expense added successfully!")
            }
            onNavigateBack()
        }
    }
    
    // Handle error state
    LaunchedEffect(uiState.error) {
        uiState.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Expense") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(top = innerPadding.calculateTopPadding() + 8.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Today's Total Display (Simple)
            if (uiState.todayTotal > 0) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = AppColors.Success.copy(alpha = 0.1f)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Today's Total",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = CurrencyUtils.formatPaiseToRupeeString(uiState.todayTotal),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = AppColors.Primary
                        )
                    }
                }
            }

            // Expense Title
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { onEvent(AddExpenseUiEvent.NameChanged(it)) },
                label = { Text("Expense Title") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.name.isBlank() && uiState.error != null
            )

            // Amount
            OutlinedTextField(
                value = uiState.amount,
                onValueChange = { onEvent(AddExpenseUiEvent.AmountChanged(it)) },
                label = { Text("Amount") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                prefix = { Text("₹") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = uiState.amount.isBlank() && uiState.error != null
            )

            // Category Label
            Text(
                text = "Category",
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // Simple Category Selector
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onEvent(AddExpenseUiEvent.ToggleCategoryPicker) },
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = uiState.category.name,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Icon(
                        imageVector = if (uiState.showCategoryPicker) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select Category"
                    )
                }
            }

            // Category Options (Simple List)
            AnimatedVisibility(visible = uiState.showCategoryPicker) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(8.dp)) {
                        // Use all categories for now to ensure it works
                        Category.entries.forEach { category ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onEvent(AddExpenseUiEvent.CategoryChanged(category))
                                        onEvent(AddExpenseUiEvent.ToggleCategoryPicker)
                                    }
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = category.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    modifier = Modifier.weight(1f)
                                )
                                if (category == uiState.category) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = "Selected",
                                        tint = AppColors.Primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
            
            // Debug: Show current category picker state
            Text(
                text = "Category picker visible: ${uiState.showCategoryPicker}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Notes
            OutlinedTextField(
                value = uiState.notes,
                onValueChange = { onEvent(AddExpenseUiEvent.NotesChanged(it)) },
                label = { Text("Notes (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                maxLines = 2
            )

            // Date
            OutlinedTextField(
                value = uiState.date,
                onValueChange = { },
                label = { Text("Date") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                readOnly = true,
                trailingIcon = {
                    Icon(Icons.Default.DateRange, contentDescription = "Pick Date")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Save Button (Simple)
            Button(
                onClick = { onEvent(AddExpenseUiEvent.Save) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = !uiState.isSaving && uiState.name.isNotBlank() && uiState.amount.isNotBlank(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = AppColors.Primary
                )
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Save Expense")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddExpenseScreenPreview() {
    ZoExpenseTrackerTheme {
        AddExpenseScreen(
            uiState = AddExpenseUiState(
                name = "Office Lunch",
                amount = "250.50",
                category = Category.Other,

                notes = "Team lunch at nearby restaurant",
                date = "2024-01-15",
                todayTotal = 125000, // ₹1,250.00 in paise
                isSaving = false,
                error = null
            ),
            onEvent = {}
        )
    }
}
