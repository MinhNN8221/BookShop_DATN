package com.example.BookShopApp.data.model.response.product

import com.example.BookShopApp.data.model.Product

data class ProductState(
    val products: List<Product>?,
    val isDefaultState: Boolean
)