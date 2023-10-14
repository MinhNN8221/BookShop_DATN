package com.example.BookShopApp.data.model.response.product

import com.example.BookShopApp.data.model.Product
import com.google.gson.annotations.SerializedName

data class ProductsByAuthor(
    @SerializedName("count") var count: Int,
    @SerializedName("rows") var products: List<Product>,
)
