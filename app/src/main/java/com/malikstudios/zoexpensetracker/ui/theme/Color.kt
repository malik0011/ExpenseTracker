package com.malikstudios.zoexpensetracker.ui.theme

import androidx.compose.ui.graphics.Color

// Base Material Design Colors
val Purple80 = Color(0xFFD0BCFF)
val PurpleGrey80 = Color(0xFFCCC2DC)
val Pink80 = Color(0xFFEFB8C8)

val Purple40 = Color(0xFF6650a4)
val PurpleGrey40 = Color(0xFF625b71)
val Pink40 = Color(0xFF7D5260)

// Money App Theme Colors - Professional Financial App Palette
object AppColors {
    // Primary Colors - Deep Financial Blue
    val Primary = Color(0xFF1E3A8A)          // Deep Blue
    val PrimaryVariant = Color(0xFF1E40AF)   // Slightly lighter blue
    val Secondary = Color(0xFF059669)        // Money Green
    val SecondaryVariant = Color(0xFF10B981) // Lighter green
    
    // Accent Colors
    val Accent = Color(0xFFF59E0B)           // Gold/Amber for highlights
    val AccentLight = Color(0xFFFBBF24)      // Light amber
    
    // Status Colors
    val Success = Color(0xFF10B981)          // Green for positive amounts/success
    val Error = Color(0xFFEF4444)            // Red for negative amounts/errors
    val Warning = Color(0xFFF59E0B)          // Amber for warnings
    val Info = Color(0xFF3B82F6)             // Blue for information
    
    // Expense Category Colors
    val CategoryStaff = Color(0xFF8B5CF6)     // Purple
    val CategoryTravel = Color(0xFF06B6D4)    // Cyan
    val CategoryFood = Color(0xFFEF4444)      // Red
    val CategoryUtility = Color(0xFF10B981)   // Green
    val CategoryOther = Color(0xFF6B7280)     // Gray
    
    // Background Colors
    val Background = Color(0xFFFAFAFA)        // Light gray background
    val Surface = Color(0xFFFFFFFF)           // White surface
    val SurfaceVariant = Color(0xFFF3F4F6)    // Light gray variant
    
    // Text Colors
    val OnPrimary = Color(0xFFFFFFFF)         // White on primary
    val OnSecondary = Color(0xFFFFFFFF)       // White on secondary
    val OnBackground = Color(0xFF111827)      // Dark text on background
    val OnSurface = Color(0xFF111827)         // Dark text on surface
    val OnSurfaceVariant = Color(0xFF6B7280)  // Gray text variant
    
    // Chart Colors
    val ChartColors = listOf(
        Color(0xFF8B5CF6), // Purple
        Color(0xFF06B6D4), // Cyan
        Color(0xFFEF4444), // Red
        Color(0xFF10B981), // Green
        Color(0xFFF59E0B), // Amber
        Color(0xFFEC4899), // Pink
        Color(0xFF8B5CF6)  // Purple variant
    )
}

// Legacy colors for compatibility
val primary = AppColors.Primary
val secondary = AppColors.Secondary
val error = AppColors.Error
val success = AppColors.Success
