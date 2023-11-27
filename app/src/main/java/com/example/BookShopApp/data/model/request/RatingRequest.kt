package com.example.BookShopApp.data.model.request

import com.example.BookShopApp.data.model.Customer
import com.example.BookShopApp.data.model.Product
import com.google.gson.annotations.SerializedName

data class RatingRequest(
    val id: Int?=null,
    val comment: String?=null,
    val ratingLevel: Int?=null,
    val bookId: Int?=null,
    val userId: Int?=null,
    val orderId:Int?=null,
)
