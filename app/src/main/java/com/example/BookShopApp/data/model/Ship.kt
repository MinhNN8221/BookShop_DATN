package com.example.BookShopApp.data.model

import com.google.gson.annotations.SerializedName

data class Ship(
    @SerializedName("id")
    val shippingId: Int,
    @SerializedName("shippingType")
    val shippingType: String,
    @SerializedName("shippingCost")
    val shippingCost: String,
)
