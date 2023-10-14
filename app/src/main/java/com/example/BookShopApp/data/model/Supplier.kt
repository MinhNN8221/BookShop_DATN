package com.example.BookShopApp.data.model

import com.google.gson.annotations.SerializedName

data class Supplier(
    @SerializedName("supplier_id")
    var supplier_id: Int,
    @SerializedName("supplier_name")
    var supplier_name: String,
)
