package com.malikstudios.zoexpensetracker.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// Enhanced Dark Color Scheme for Financial App
private val DarkColorScheme = darkColorScheme(
    primary = AppColors.Primary,
    onPrimary = AppColors.OnPrimary,
    secondary = AppColors.Secondary,
    onSecondary = AppColors.OnSecondary,
    tertiary = AppColors.Accent,
    onTertiary = Color.White,
    background = Color(0xFF0F172A),      // Dark blue-gray
    onBackground = Color(0xFFF8FAFC),    // Light text
    surface = Color(0xFF1E293B),         // Dark surface
    onSurface = Color(0xFFF8FAFC),       // Light text on surface
    surfaceVariant = Color(0xFF334155),  // Slightly lighter surface
    onSurfaceVariant = Color(0xFFCBD5E1), // Medium light text
    error = AppColors.Error,
    onError = Color.White,
    outline = Color(0xFF64748B),         // Border color
    outlineVariant = Color(0xFF475569)   // Subtle border
)

// Enhanced Light Color Scheme for Financial App
private val LightColorScheme = lightColorScheme(
    primary = AppColors.Primary,
    onPrimary = AppColors.OnPrimary,
    secondary = AppColors.Secondary,
    onSecondary = AppColors.OnSecondary,
    tertiary = AppColors.Accent,
    onTertiary = Color.White,
    background = AppColors.Background,
    onBackground = AppColors.OnBackground,
    surface = AppColors.Surface,
    onSurface = AppColors.OnSurface,
    surfaceVariant = AppColors.SurfaceVariant,
    onSurfaceVariant = AppColors.OnSurfaceVariant,
    error = AppColors.Error,
    onError = Color.White,
    outline = Color(0xFFD1D5DB),         // Light border
    outlineVariant = Color(0xFFE5E7EB)   // Subtle light border
)

@Composable
fun ZoExpenseTrackerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false, // Disabled to maintain consistent financial app branding
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}