package com.example.BookShopApp.data.model.response.product

import com.google.gson.annotations.SerializedName

class ProductInfo(
    @SerializedName("product_id")
    var productId: Int,
    @SerializedName("name")
    var name: String,
    @SerializedName("description")
    var description: String,
    @SerializedName("price")
    var price: String,
    @SerializedName("discounted_price")
    var discountedPrice: String,
    @SerializedName("quantity") var quantity: Int,
    @SerializedName("quantitySold") var quantitySold: Int,
    @SerializedName("thumbnail")
    var thumbnail: String,
    @SerializedName("wishlist")
    var wishlist: Int,
)