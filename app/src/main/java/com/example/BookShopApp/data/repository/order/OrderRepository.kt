package com.example.BookShopApp.data.repository.order

import com.example.BookShopApp.data.model.response.Message
import com.example.BookShopApp.data.model.OrderDetail
import com.example.BookShopApp.data.model.response.order.OrderList
import retrofit2.Response


interface OrderRepository {
    suspend fun getOrderHistory(): Response<OrderList>?
    suspend fun getOrderDetail(orderId: Int): Response<OrderDetail>?
    suspend fun createOrder(
        cartId: String,
        shippingId: Int,
        receiverId: Int,
    ): Response<Message>
}