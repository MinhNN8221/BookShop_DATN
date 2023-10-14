package com.example.BookShopApp.data.model.response.product

import com.google.gson.annotations.SerializedName

data class BookInHomeList(
    @SerializedName("count") var count: Int?,
    @SerializedName("products") var products: List<BookInHome>,
)
