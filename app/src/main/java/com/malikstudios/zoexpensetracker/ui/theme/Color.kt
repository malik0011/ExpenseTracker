package com.malikstudios.zoexpensetracker.ui.theme

import androidx.compose.ui.graphics.Color


// Money App Theme Colors - Professional Financial App Palette
object AppColors {
    // Primary Colors - Deep Financial Blue
    val Primary = Color(0xFF1E3A8A)          // Deep Blue
    val Secondary = Color(0xFF059669)        // Money Green

    // Accent Colors
    val Accent = Color(0xFFF59E0B)           // Gold/Amber for highlights

    // Status Colors
    val Success = Color(0xFF10B981)          // Green for positive amounts/success
    val Error = Color(0xFFEF4444)            // Red for negative amounts/errors
    val Warning = Color(0xFFF59E0B)          // Amber for warnings
    val Info = Color(0xFF3B82F6)             // Blue for information

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
    val Amber = Color(0xFFF59E0B) // Amber // Gray text variant

}

val error = AppColors.Error