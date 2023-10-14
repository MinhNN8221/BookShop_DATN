package com.example.BookShopApp.utils.format

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FormatDate {
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDate(str: String?): String {
        val str_new = LocalDate.parse(str?.substring(0, 10))
        return str_new.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateOfBirth(inputDate: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy")
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = LocalDate.parse(inputDate, inputFormatter)
        return outputFormatter.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateOfBirthView(inputDate: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = LocalDate.parse(inputDate, inputFormatter)
        return outputFormatter.format(date)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun formatDateReverse(inputDate: String): String {
        val inputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val date = LocalDate.parse(inputDate, inputFormatter)
        return outputFormatter.format(date)
    }
}