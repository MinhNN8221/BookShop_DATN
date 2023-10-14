package com.example.BookShopApp.data.model

import android.text.TextUtils
import com.google.gson.annotations.SerializedName

data class Receiver(
    @SerializedName("receiver_id") var receiverId: Int?=null,
    @SerializedName("receiver_name") var receiverName: String,
    @SerializedName("receiver_phone") var receiverPhone: String,
    @SerializedName("receiver_address") var receiverAddress: String,
    @SerializedName("isDefault") var isDefault: Int?=null,
    @SerializedName("customer_id") var customerId: Int?=null,
){
    fun isAddReceiverInfo():Boolean{
        return TextUtils.isEmpty(receiverName) || TextUtils.isEmpty(receiverPhone) || TextUtils.isEmpty(receiverAddress)
    }
    fun isValidPhone(): Boolean {
        val pattern = Regex("^0\\d{9}$")
        return pattern.matches(receiverPhone)
    }
}
