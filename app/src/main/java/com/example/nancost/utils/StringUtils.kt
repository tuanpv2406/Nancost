package com.example.nancost.utils

import java.text.DecimalFormat
import java.text.NumberFormat

object StringUtils {
    fun formatCurrency(currency: Int): String {
        val formatter: NumberFormat = DecimalFormat("#,###")
        return formatter.format(currency)
    }

    fun currencyToString(currency: String): String {
        return currency.replace(",", "")
    }
}