package com.example.BookShopApp.data.model

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("id") var categoryId: Int?,
    @SerializedName("name") var name: String?,
    @SerializedName("description") var description: String?,
)
