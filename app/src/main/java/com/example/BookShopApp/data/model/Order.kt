package com.example.BookShopApp.data.model

import com.google.gson.annotations.SerializedName

data class Order(
    @SerializedName("order_id") var orderId: Int?,
    @SerializedName("merchandise_subtotal") var merchandiseSubtotal: String?,
    @SerializedName("total_quantity") var totalQuantity: String?,
    @SerializedName("created_on") var createdOn: String?,
    @SerializedName("shipped_on") var shippedOn: String?,
    @SerializedName("customer_id") var customerId: Int?,
    @SerializedName("address") var address: String?,
    @SerializedName("receiver_name") var receiverName: String?,
    @SerializedName("receiver_phone") var receiverPhone: String?,
    @SerializedName("shipping_id") var shippingId: Int?,
    @SerializedName("shipping_type") var shippingType: String?,
    @SerializedName("shipping_cost") var shippingCost: String?,
    @SerializedName("order_status") var orderStatus: String?,
    @SerializedName("order_total") var orderTotal: String?,
)
