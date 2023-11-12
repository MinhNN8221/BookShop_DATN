package com.example.BookShopApp.data.model

import com.example.BookShopApp.data.model.response.order.OrderDetailProduct
import com.google.gson.annotations.SerializedName

data class OrderDetail(
    @SerializedName("order_id") var orderId: Int?,
    @SerializedName("merchandise_subtotal") var merchandiseSubtotal: String?,
    @SerializedName("created_on") var createdOn: String?,
    @SerializedName("shipped_on") var shippedOn: String?,
    @SerializedName("status") var status: Int?,
    @SerializedName("customer_id") var customerId: Int?,
    @SerializedName("address") var address: String?,
    @SerializedName("receiver_name") var receiverName: String?,
    @SerializedName("receiver_phone") var receiverPhone: String?,
    @SerializedName("shipping_id") var shippingId: Int?,
    @SerializedName("payment_method") var paymentMethod: String?,
    @SerializedName("shipping_type") var shippingType: String?,
    @SerializedName("shipping_cost") var shippingCost: String?,
    @SerializedName("order_total") var orderTotal: String?,
    @SerializedName("products") var products: List<OrderDetailProduct>,
)
