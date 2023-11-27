package com.example.BookShopApp.data.model.response.order

import com.example.BookShopApp.data.model.Order

data class OrderHistory(
    val header: String?,
    val order: Order?,
):java.io.Serializable