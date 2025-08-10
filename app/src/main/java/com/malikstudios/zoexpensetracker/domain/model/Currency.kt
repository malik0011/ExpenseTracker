package com.malikstudios.zoexpensetracker.domain.model

/**
 * Represents a currency with its metadata
 */
data class Currency(
    val code: String,           // ISO 4217 currency code
    val symbol: String,         // Currency symbol
    val name: String,           // Full currency name
    val decimalPlaces: Int = 2, // Number of decimal places
    val symbolPosition: SymbolPosition = SymbolPosition.BEFORE
)

enum class SymbolPosition {
    BEFORE, // $100
    AFTER   // 100₹
}

/**
 * Popular currencies for expense tracking apps
 */
object SupportedCurrencies {
    val USD = Currency("USD", "$", "US Dollar")
    val EUR = Currency("EUR", "€", "Euro")
    val GBP = Currency("GBP", "£", "British Pound")
    val INR = Currency("INR", "₹", "Indian Rupee", symbolPosition = SymbolPosition.BEFORE)
    val JPY = Currency("JPY", "¥", "Japanese Yen", decimalPlaces = 0)
    val CNY = Currency("CNY", "¥", "Chinese Yuan")
    val CAD = Currency("CAD", "C$", "Canadian Dollar")
    val AUD = Currency("AUD", "A$", "Australian Dollar")
    val CHF = Currency("CHF", "Fr", "Swiss Franc")
    val SGD = Currency("SGD", "S$", "Singapore Dollar")
    val HKD = Currency("HKD", "HK$", "Hong Kong Dollar")
    val NOK = Currency("NOK", "kr", "Norwegian Krone", symbolPosition = SymbolPosition.AFTER)
    val SEK = Currency("SEK", "kr", "Swedish Krona", symbolPosition = SymbolPosition.AFTER)
    val DKK = Currency("DKK", "kr", "Danish Krone", symbolPosition = SymbolPosition.AFTER)
    val NZD = Currency("NZD", "NZ$", "New Zealand Dollar")
    val ZAR = Currency("ZAR", "R", "South African Rand")
    val BRL = Currency("BRL", "R$", "Brazilian Real")
    val RUB = Currency("RUB", "₽", "Russian Ruble", symbolPosition = SymbolPosition.AFTER)
    val KRW = Currency("KRW", "₩", "South Korean Won", decimalPlaces = 0)
    val MXN = Currency("MXN", "$", "Mexican Peso")
    val AED = Currency("AED", "د.إ", "UAE Dirham", symbolPosition = SymbolPosition.AFTER)
    
    /**
     * List of all supported currencies ordered by popularity
     */
    val ALL = listOf(
        USD, EUR, GBP, INR, JPY, CNY, CAD, AUD, CHF, SGD,
        HKD, NOK, SEK, DKK, NZD, ZAR, BRL, RUB, KRW, MXN, AED
    )
    
    /**
     * Most commonly used currencies for quick access
     */
    val POPULAR = listOf(USD, EUR, GBP, INR, JPY, CNY, CAD, AUD)
    
    /**
     * Default currency (can be changed based on user's locale)
     */
    val DEFAULT = INR
    
    /**
     * Find currency by code
     */
    fun findByCode(code: String): Currency? = ALL.find { it.code.equals(code, ignoreCase = true) }
    
    /**
     * Get currency by code or return default
     */
    fun getByCodeOrDefault(code: String?): Currency = 
        code?.let { findByCode(it) } ?: DEFAULT
}
