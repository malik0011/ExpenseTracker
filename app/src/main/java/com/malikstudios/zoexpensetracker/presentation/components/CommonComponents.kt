package com.malikstudios.zoexpensetracker.presentation.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp


import com.malikstudios.zoexpensetracker.ui.theme.AppColors
import com.malikstudios.zoexpensetracker.utils.CurrencyUtils


/**
 * Animated amount display card with professional financial styling
 */
@Composable
fun AmountDisplayCard(
    title: String,
    amount: Long,
    modifier: Modifier = Modifier,
    isPositive: Boolean = true,
    showAnimation: Boolean = true
) {
    val animatedAmount by animateFloatAsState(
        targetValue = if (showAnimation) amount.toFloat() else amount.toFloat(),
        animationSpec = tween(durationMillis = 1000),
        label = "amount_animation"
    )
    
    val backgroundColor by animateColorAsState(
        targetValue = if (isPositive) AppColors.Success.copy(alpha = 0.1f) else AppColors.Error.copy(alpha = 0.1f),
        label = "background_color"
    )
    
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = CurrencyUtils.formatPaiseToRupeeString(animatedAmount.toLong()),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = if (isPositive) AppColors.Success else AppColors.Error
            )
        }
    }
}

///**
// * Enhanced category selector with chips and icons
// */
//@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
//@Composable
//fun CategorySelector(
//    selectedCategory: Category,
//    onCategoryChanged: (Category) -> Unit,
//    modifier: Modifier = Modifier,
//    showPopular: Boolean = true
//) {
//    var expanded by remember { mutableStateOf(false) }
//
//    Column(modifier = modifier) {
//        // Selected category display
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .clickable { expanded = !expanded },
//            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
//        ) {
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(16.dp),
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Spacer(modifier = Modifier.width(12.dp))
//                Column(modifier = Modifier.weight(1f)) {
//                    Text(
//                        text = selectedCategory.name,
//                        style = MaterialTheme.typography.titleMedium,
//                        fontWeight = FontWeight.Medium
//                    )
//                }
//                Icon(
//                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
//                    contentDescription = if (expanded) "Collapse" else "Expand",
//                    modifier = Modifier.rotate(animateFloatAsState(
//                        targetValue = if (expanded) 180f else 0f,
//                        label = "arrow_rotation"
//                    ).value)
//                )
//            }
//        }
//
//        // Category options
//        AnimatedVisibility(
//            visible = expanded,
//            enter = expandVertically() + fadeIn(),
//            exit = shrinkVertically() + fadeOut()
//        ) {
//            Card(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 4.dp),
//                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
//            ) {
//                Column(modifier = Modifier.padding(8.dp)) {
//                    if (showPopular) {
//                        Text(
//                            text = "Popular",
//                            style = MaterialTheme.typography.labelMedium,
//                            color = MaterialTheme.colorScheme.primary,
//                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//                        )
//                        FlowRow(
//                            horizontalArrangement = Arrangement.spacedBy(8.dp),
//                            modifier = Modifier.padding(horizontal = 8.dp)
//                        ) {
//                            Cate.POPULAR.forEach { category ->
//                                CategoryChip(
//                                    category = category,
//                                    isSelected = category == selectedCategory,
//                                    onClick = {
//                                        onCategoryChanged(category)
//                                        expanded = false
//                                    }
//                                )
//                            }
//                        }
//
//                        Text(
//                            text = "All Categories",
//                            style = MaterialTheme.typography.labelMedium,
//                            color = MaterialTheme.colorScheme.primary,
//                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//                        )
//                    }
//
//                    LazyColumn(
//                        modifier = Modifier.height(200.dp),
//                        verticalArrangement = Arrangement.spacedBy(4.dp)
//                    ) {
//                        items(ExpenseCategory.values()) { category ->
//                            CategoryListItem(
//                                category = category,
//                                isSelected = category == selectedCategory,
//                                onClick = {
//                                    onCategoryChanged(category)
//                                    expanded = false
//                                }
//                            )
//                        }
//                    }
//                }
//            }
//        }
//    }
//}

/**
 * Category chip component
 */
@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun CategoryChip(
//    category: ExpenseCategory,
//    isSelected: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    FilterChip(
//        selected = isSelected,
//        onClick = onClick,
//        label = {
//            Row(verticalAlignment = Alignment.CenterVertically) {
//                Text(text = category.icon)
//                Spacer(modifier = Modifier.width(4.dp))
//                Text(text = category.displayName)
//            }
//        },
//        modifier = modifier,
//        leadingIcon = if (isSelected) {
//            { Icon(Icons.Default.Check, contentDescription = "Selected") }
//        } else null
//    )
//}
//
///**
// * Category list item for expanded view
// */
//@Composable
//fun CategoryListItem(
//    category: ExpenseCategory,
//    isSelected: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Surface(
//        modifier = modifier
//            .fillMaxWidth()
//            .clickable { onClick() },
//        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
//        shape = RoundedCornerShape(8.dp)
//    ) {
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(12.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Box(
//                modifier = Modifier
//                    .size(40.dp)
//                    .background(
//                        getCategoryColor(category.name).copy(alpha = 0.2f),
//                        CircleShape
//                    ),
//                contentAlignment = Alignment.Center
//            ) {
//                Text(
//                    text = category.icon,
//                    style = MaterialTheme.typography.titleMedium
//                )
//            }
//            Spacer(modifier = Modifier.width(12.dp))
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    text = category.displayName,
//                    style = MaterialTheme.typography.titleSmall,
//                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
//                )
//                Text(
//                    text = category.description,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant,
//                    maxLines = 2,
//                    overflow = TextOverflow.Ellipsis
//                )
//            }
//            if (isSelected) {
//                Icon(
//                    imageVector = Icons.Default.Check,
//                    contentDescription = "Selected",
//                    tint = MaterialTheme.colorScheme.primary
//                )
//            }
//        }
//    }
//}



/**
 * Loading shimmer effect for better UX
 */
@Composable
fun LoadingShimmer(
    modifier: Modifier = Modifier
) {
    // This is a placeholder for shimmer effect
    // In a real app, you'd implement a proper shimmer animation
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        shape = RoundedCornerShape(8.dp)
    ) {
        // Empty content for shimmer
    }
}
