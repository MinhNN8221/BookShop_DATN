package com.example.BookShopApp.data.model

import com.google.gson.annotations.SerializedName

data class CartItem(
    @SerializedName("item_id") var itemId: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("price") var price: String ?= null,
    @SerializedName("quantity") var quantity: Int,
    @SerializedName("quantityBook") var quantityBook: Int,
    @SerializedName("quantitySold") var quantitySold: Int,
    @SerializedName("product_id") var productId: Int? = null,
    @SerializedName("sub_total") var subTotal: String = "",
    @SerializedName("added_on") var addedOn: String? = null,
    @SerializedName("discounted_price") var discountedPrice: String? = null,
    @SerializedName("wishlist") var wishlist: Int? = null,
    @SerializedName("image") var image: String? = null,
)
