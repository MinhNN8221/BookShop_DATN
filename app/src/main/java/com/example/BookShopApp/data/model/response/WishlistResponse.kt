package com.example.BookShopApp.data.model.response
import com.example.BookShopApp.data.model.Wishlist
import com.google.gson.annotations.SerializedName

data class WishlistResponse(
    @SerializedName("count") val count : Int,
    @SerializedName("products") val wishlist: List<Wishlist>,
)
