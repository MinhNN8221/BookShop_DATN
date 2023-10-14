package com.example.BookShopApp.data.model.response

import com.example.BookShopApp.data.model.Product

data class HistorySearch(
    val historyLocal: String?,
    val historySuggest: Product?,
)