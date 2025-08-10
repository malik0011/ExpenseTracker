package com.malikstudios.zoexpensetracker.utils

import com.malikstudios.zoexpensetracker.domain.model.RupeeCurrency
import kotlin.math.pow

/**
 * Utility object for Rupee currency formatting and parsing
 */
object CurrencyUtils {
    
    /**
     * Parses amount text to paise (smallest Rupee unit)
     * Supports decimal input like "100.50" -> 10050 paise
     */
    fun parseToPaise(amountText: String): Result<Long> = runCatching {
        if (amountText.isBlank()) return@runCatching 0L
        
        val parts = amountText.trim().split(".")
        val rupees = parts[0].toLong()
        val paise = when {
            parts.size == 1 -> 0L
            parts[1].length >= RupeeCurrency.DECIMAL_PLACES -> {
                parts[1].substring(0, RupeeCurrency.DECIMAL_PLACES).toLong()
            }
            else -> {
                val padded = parts[1].padEnd(RupeeCurrency.DECIMAL_PLACES, '0')
                padded.toLong()
            }
        }
        
        rupees * 100 + paise
    }
    
    /**
     * Formats paise to Rupee string with symbol
     * Example: 10050 -> "₹100.50"
     */
    fun formatPaiseToRupeeString(paise: Long): String {
        val rupees = paise / 100
        val paisePart = paise % 100
        val paiseStr = paisePart.toString().padStart(RupeeCurrency.DECIMAL_PLACES, '0')
        
        return "${RupeeCurrency.SYMBOL}$rupees.$paiseStr"
    }
    
    /**
     * Formats paise to Rupee string without symbol
     * Example: 10050 -> "100.50"
     */
    fun formatPaiseToRupeeStringWithoutSymbol(paise: Long): String {
        val rupees = paise / 100
        val paisePart = paise % 100
        val paiseStr = paisePart.toString().padStart(RupeeCurrency.DECIMAL_PLACES, '0')
        
        return "$rupees.$paiseStr"
    }
    
    /**
     * Validate amount input for Rupees
     */
    fun isValidAmount(amountText: String): Boolean {
        return parseToPaise(amountText).isSuccess
    }
}
