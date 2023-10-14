package com.example.BookShopApp.data.model.response.author

import com.example.BookShopApp.data.model.Author
import com.google.gson.annotations.SerializedName

data class AuthorInfor(
    @SerializedName("result" ) var author : Author,
)