package com.example.BookShopApp.data.model.response.product

import com.google.gson.annotations.SerializedName

data class BannerList(
    @SerializedName("count") var count: Int? = null,
    @SerializedName("products") var products: List<Banner>,
)
