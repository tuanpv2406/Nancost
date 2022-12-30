package com.example.nancost.utils

import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Currency

object StringUtils {
    fun formatCurrency(currency: Int): String {
        val formatter: NumberFormat = DecimalFormat("#,###")
        return formatter.format(currency)
    }
}