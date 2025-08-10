package com.malikstudios.zoexpensetracker.utils

import com.malikstudios.zoexpensetracker.domain.model.Currency
import com.malikstudios.zoexpensetracker.domain.model.SupportedCurrencies
import com.malikstudios.zoexpensetracker.domain.model.SymbolPosition
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs
import kotlin.math.pow

/**
 * Enhanced utility object for multi-currency support and formatting
 */
object CurrencyUtils {
    
    /**
     * Parses amount text to smallest currency unit (e.g., paise for INR, cents for USD)
     * Supports different decimal places based on currency
     */
    fun parseToSmallestUnit(
        amountText: String, 
        currency: Currency = SupportedCurrencies.DEFAULT
    ): Result<Long> {
        val s = amountText.trim()
        if (s.isEmpty()) return Result.failure(IllegalArgumentException("Empty amount"))
        
        val negative = s.startsWith("-")
        val clean = if (negative) s.substring(1) else s
        
        return try {
            val parts = clean.split('.', limit = 2)
            val majorPart = parts.getOrNull(0)?.filter { it.isDigit() } ?: "0"
            val minorPartRaw = parts.getOrNull(1) ?: ""
            
            // Handle different decimal places based on currency
            val multiplier = 10.0.pow(currency.decimalPlaces).toLong()
            val minorPart = when {
                currency.decimalPlaces == 0 -> 0L // Currencies like JPY, KRW
                minorPartRaw.isEmpty() -> 0L
                minorPartRaw.length >= currency.decimalPlaces -> {
                    minorPartRaw.substring(0, currency.decimalPlaces).toLong()
                }
                else -> {
                    // Pad with zeros
                    val padded = minorPartRaw.padEnd(currency.decimalPlaces, '0')
                    padded.toLong()
                }
            }
            
            val total = majorPart.toLong() * multiplier + minorPart
            if (negative) Result.success(-total) else Result.success(total)
        } catch (e: NumberFormatException) {
            Result.failure(IllegalArgumentException("Invalid amount format"))
        }
    }

    /**
     * Formats amount in smallest unit to currency string with proper symbol positioning
     */
    fun formatAmountWithCurrency(
        smallestUnitAmount: Long, 
        currency: Currency = SupportedCurrencies.DEFAULT
    ): String {
        val negative = smallestUnitAmount < 0
        val absAmount = abs(smallestUnitAmount)
        
        val multiplier = 10.0.pow(currency.decimalPlaces).toLong()
        val majorPart = absAmount / multiplier
        val minorPart = absAmount % multiplier
        
        val formattedAmount = when (currency.decimalPlaces) {
            0 -> {
                // Currencies without decimal places (JPY, KRW)
                NumberFormat.getNumberInstance(Locale.getDefault()).format(majorPart)
            }
            else -> {
                val minorStr = minorPart.toString().padStart(currency.decimalPlaces, '0')
                val majorStr = NumberFormat.getNumberInstance(Locale.getDefault()).format(majorPart)
                "$majorStr.$minorStr"
            }
        }
        
        val negativePrefix = if (negative) "-" else ""
        
        return when (currency.symbolPosition) {
            SymbolPosition.BEFORE -> "$negativePrefix${currency.symbol}$formattedAmount"
            SymbolPosition.AFTER -> "$negativePrefix$formattedAmount${currency.symbol}"
        }
    }

    /**
     * Legacy method for backward compatibility (INR specific)
     */
    @Deprecated("Use parseToSmallestUnit with Currency parameter", ReplaceWith("parseToSmallestUnit(amountText, SupportedCurrencies.INR)"))
    fun parseToPaise(amountText: String): Result<Long> = parseToSmallestUnit(amountText, SupportedCurrencies.INR)

    /**
     * Legacy method for backward compatibility (INR specific)
     */
    @Deprecated("Use formatAmountWithCurrency with Currency parameter", ReplaceWith("formatAmountWithCurrency(paise, SupportedCurrencies.INR)"))
    fun formatPaiseToRupeeString(paise: Long): String = formatAmountWithCurrency(paise, SupportedCurrencies.INR)
    
    /**
     * Format amount for display in lists (compact format)
     */
    fun formatCompactAmount(
        smallestUnitAmount: Long, 
        currency: Currency = SupportedCurrencies.DEFAULT
    ): String {
        val absAmount = abs(smallestUnitAmount)
        val multiplier = 10.0.pow(currency.decimalPlaces).toLong()
        val majorPart = absAmount / multiplier
        
        val compactAmount = when {
            majorPart >= 1_000_000 -> {
                val millions = majorPart / 1_000_000.0
                String.format("%.1fM", millions)
            }
            majorPart >= 1_000 -> {
                val thousands = majorPart / 1_000.0
                String.format("%.1fK", thousands)
            }
            else -> formatAmountWithCurrency(smallestUnitAmount, currency)
        }
        
        return if (smallestUnitAmount < 0) "-$compactAmount" else compactAmount
    }
    
    /**
     * Get appropriate currency based on user's locale
     */
    fun getCurrencyForLocale(locale: Locale = Locale.getDefault()): Currency {
        return when (locale.country.uppercase()) {
            "US" -> SupportedCurrencies.USD
            "GB" -> SupportedCurrencies.GBP
            "IN" -> SupportedCurrencies.INR
            "JP" -> SupportedCurrencies.JPY
            "CN" -> SupportedCurrencies.CNY
            "CA" -> SupportedCurrencies.CAD
            "AU" -> SupportedCurrencies.AUD
            "CH" -> SupportedCurrencies.CHF
            "SG" -> SupportedCurrencies.SGD
            "HK" -> SupportedCurrencies.HKD
            "NO" -> SupportedCurrencies.NOK
            "SE" -> SupportedCurrencies.SEK
            "DK" -> SupportedCurrencies.DKK
            "NZ" -> SupportedCurrencies.NZD
            "ZA" -> SupportedCurrencies.ZAR
            "BR" -> SupportedCurrencies.BRL
            "RU" -> SupportedCurrencies.RUB
            "KR" -> SupportedCurrencies.KRW
            "MX" -> SupportedCurrencies.MXN
            "AE" -> SupportedCurrencies.AED
            else -> {
                // For EU countries, try to detect Euro usage
                if (locale.country in listOf("DE", "FR", "IT", "ES", "NL", "BE", "AT", "PT", "IE", "FI", "GR", "LU", "SI", "SK", "EE", "LV", "LT", "CY", "MT")) {
                    SupportedCurrencies.EUR
                } else {
                    SupportedCurrencies.DEFAULT
                }
            }
        }
    }
    
    /**
     * Validate amount input for a specific currency
     */
    fun isValidAmount(amountText: String, currency: Currency = SupportedCurrencies.DEFAULT): Boolean {
        return parseToSmallestUnit(amountText, currency).isSuccess
    }
    
    /**
     * Convert amount between currencies (mock conversion for now)
     * In a real app, this would use live exchange rates
     */
    fun convertAmount(
        amount: Long,
        fromCurrency: Currency,
        toCurrency: Currency,
        exchangeRate: Double = 1.0
    ): Long {
        // This is a mock implementation
        // In production, you'd fetch real exchange rates from an API
        return (amount * exchangeRate).toLong()
    }
}
