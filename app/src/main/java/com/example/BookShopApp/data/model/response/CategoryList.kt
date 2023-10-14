package com.example.BookShopApp.data.model.response

import com.example.BookShopApp.data.model.Category
import com.google.gson.annotations.SerializedName

data class CategoryList(
    @SerializedName("count") var count: Int?,
    @SerializedName("rows")
    var categories: List<Category>,
)
