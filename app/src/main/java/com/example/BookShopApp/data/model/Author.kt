package com.example.BookShopApp.data.model

import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("id") var authorId: Int,
    @SerializedName("name") var authorName: String,
    @SerializedName("description") var authorDescription: String,
    @SerializedName("avatar") var authorAvatar: String,
)
