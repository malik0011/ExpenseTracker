package com.malikstudios.zoexpensetracker.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.malikstudios.zoexpensetracker.domain.model.Category
import com.malikstudios.zoexpensetracker.presentation.AddExpenseUiEvent
import com.malikstudios.zoexpensetracker.presentation.DocumentItem
import com.malikstudios.zoexpensetracker.presentation.HomeUiEvent
import com.malikstudios.zoexpensetracker.presentation.HomeUiState
import com.malikstudios.zoexpensetracker.ui.theme.ZoExpenseTrackerTheme

// ---------- Stateless Home Screen ----------
@OptIn(ExperimentalAnimationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    innerPadding: PaddingValues,
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Home") }
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = !uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                FloatingActionButton(
                    onClick = { onEvent(HomeUiEvent.CreateNew) }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Create New")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding() + 8.dp)
                .padding(bottom = 16.dp),
        ) {

                // Recent Items Row
                if (uiState.recentItems.isNotEmpty()) {
                    Text(
                        text = "Recent",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(16.dp)
                    )
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.recentItems) { item ->
                            RecentCard(item) { onEvent(HomeUiEvent.OpenItem(item.id)) }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                }

                // All Items List/Grid
                if (uiState.isGrid) {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(uiState.allItems) { item ->
                            DocumentCard(item) { onEvent(HomeUiEvent.OpenItem(item.id)) }
                        }
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.allItems) { item ->
                            DocumentListItem(item) { onEvent(HomeUiEvent.OpenItem(item.id)) }
                        }
                    }
                }

//        }

        }
    }
}

// ---------- Reusable Item Components ----------
@Composable
fun RecentCard(item: DocumentItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .size(120.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun DocumentCard(item: DocumentItem, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(item.title, style = MaterialTheme.typography.titleMedium)
            Text(item.subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
fun DocumentListItem(item: DocumentItem, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(item.title) },
        supportingContent = { Text(item.subtitle) },
        modifier = Modifier.clickable(onClick = onClick)
    )
}

@Preview
@Composable
private fun HomePreview() {
    ZoExpenseTrackerTheme {
        Column(Modifier.fillMaxSize()) {
            HomeScreen(
                innerPadding = PaddingValues(0.dp),
                uiState = HomeUiState(
                    recentItems = listOf(
                       DocumentItem("1", "Recent Item 1", "Subtitle 1", "₹100", Category.Other, System.currentTimeMillis()),
                       DocumentItem("2", "Recent Item 2", "Subtitle 2", "₹140", Category.Other, System.currentTimeMillis()),
                    ),
                    allItems = listOf(
                        DocumentItem("1", "Recent Item 1", "Subtitle 1", "₹100", Category.Other, System.currentTimeMillis()),
                        DocumentItem("2", "Recent Item 2", "Subtitle 2", "₹140", Category.Other, System.currentTimeMillis()),
                    ),
                    isGrid = false,
                    isLoading = false
                ),
                onEvent = {}
            )
        }
    }
}
