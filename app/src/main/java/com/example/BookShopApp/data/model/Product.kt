package com.example.BookShopApp.data.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("product_id")
    var product_id: Int,
    @SerializedName("name")
    val name: String?,
    @SerializedName("description")
    var description: String?,
    @SerializedName("price")
    var price: String?,
    @SerializedName("discounted_price")
    var discounted_price: String?,
    @SerializedName("quantity")
    var quantity: Int,
    @SerializedName("quantitySold")
    var quantitySold: Int,
    @SerializedName("image")
    var image: String?,
    @SerializedName("image_2")
    val image_2: String?,
    @SerializedName("thumbnail")
    var thumbnail: String?,
//    @SerializedName("display")
//    var display: Int=0,
    @SerializedName("author_id")
    var author_id: Int,
    @SerializedName("supplier_id")
    var supplier_id: Int,
    @SerializedName("category_id")
    var category_id: Int,
)
