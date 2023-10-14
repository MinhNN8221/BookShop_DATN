package com.example.BookShopApp.data.model.response.product

import com.example.BookShopApp.data.model.Author
import com.example.BookShopApp.data.model.Supplier
import com.example.BookShopApp.data.model.response.author.AuthorProInfo
import com.google.gson.annotations.SerializedName

data class ProductInfoList(
    @SerializedName("product") var product: ProductInfo,
    @SerializedName("supplier") var supplier: Supplier,
    @SerializedName("author") var author: AuthorProInfo,
)
