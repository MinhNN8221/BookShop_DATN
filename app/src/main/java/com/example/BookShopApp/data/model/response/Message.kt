package com.example.BookShopApp.data.model.response

import com.google.gson.annotations.SerializedName

data class Message(
    @SerializedName("message") var message: String,
)
