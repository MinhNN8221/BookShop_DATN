package com.example.BookShopApp.data.model.response.product

import com.google.gson.annotations.SerializedName

data class BookInHome(
    @SerializedName("product_id") var productId: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("price") var price: String? = null,
    @SerializedName("discounted_price") var discountedPrice: String? = null,
    @SerializedName("quantity") var quantity: Int,
    @SerializedName("quantitySold") var quantitySold: Int,
    @SerializedName("thumbnail") var thumbnail: String? = null,
)
