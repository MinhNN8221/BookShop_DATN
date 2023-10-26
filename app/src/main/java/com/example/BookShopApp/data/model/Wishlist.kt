package com.example.BookShopApp.data.model

import com.google.gson.annotations.SerializedName

data class Wishlist(
    @SerializedName("product_id")
    val product_id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    val description: String?,
    @SerializedName("price")
    val price: String?,
    @SerializedName("discounted_price")
    val discount: String?,
    @SerializedName("quantity") var quantity: Int,
    @SerializedName("quantitySold") var quantitySold: Int,
    @SerializedName("thumbnail")
    val thumbnail: String?,
    @SerializedName("supplierName") var supplierName: String?,
)