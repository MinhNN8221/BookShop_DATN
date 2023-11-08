package com.example.BookShopApp.data.model

import com.google.gson.annotations.SerializedName


data class Customer(
    @SerializedName("customer_id")
    var customerId: Int? = null,
    @SerializedName("name")
    var name: String = "",
    @SerializedName("email")
    var email: String = "",
    @SerializedName("password")
    var password: String = "",
    @SerializedName("new_pass")
    var newPassword: String = "",
    @SerializedName("password_again")
    var passwordAgain: String = "",
    @SerializedName("address")
    var address: String = "",
    @SerializedName("shipping_region_id")
    var shippingRegionId: Int? = null,
    @SerializedName("mob_phone")
    var mobPhone: String = "",
    @SerializedName("gender")
    var gender: String? = null,
    @SerializedName("date_of_birth")
    var dateOfBirth: String? = null,
    @SerializedName("avatar")
    var avatar: String? = null,
    @SerializedName("status")
    var status: String? = null,
)

