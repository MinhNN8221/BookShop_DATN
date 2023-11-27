package com.example.BookShopApp.data.model

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("id")
    val id: Int,
    @SerializedName("comment")
    val comment: String,
    @SerializedName("ratingLevel")
    val ratingLevel: Int,
    @SerializedName("avatarUser")
    val avatarUser: String,
    @SerializedName("nameUser")
    val nameUser: String,
    @SerializedName("createTime")
    val createTime: String,
)
