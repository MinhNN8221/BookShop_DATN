package com.example.BookShopApp.utils.format
import java.text.NumberFormat
import java.util.*

class FormatMoney {
    fun formatMoney(str: Long): String {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
        val str_new=numberFormat.format(str).replace(".", ",")
        val vndSymbol = "\u20AB" // Ký hiệu ₫ (Unicode)
        return str_new.replace(vndSymbol, "VND")
    }
}