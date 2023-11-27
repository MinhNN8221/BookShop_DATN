package com.example.BookShopApp.data.model.response.order

import com.google.gson.annotations.SerializedName

data class OrderDetailProduct(
    @SerializedName("order_id") var orderId: Int? = null,
    @SerializedName("product_id") var productId: Int? = null,
    @SerializedName("product_name") var productName: String? = null,
    @SerializedName("image") var image: String? = null,
    @SerializedName("product_description") var productDescription: String? = null,
    @SerializedName("quantity") var quantity: Int? = null,
    @SerializedName("unit_cost") var unitCost: String? = null,
    @SerializedName("subtotal") var subtotal: String? = null,
    @SerializedName("wishlist") var wishlist: Int? = null,
)