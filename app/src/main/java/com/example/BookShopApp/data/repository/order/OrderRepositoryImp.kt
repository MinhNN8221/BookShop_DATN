package com.example.BookShopApp.data.repository.order

import com.example.BookShopApp.data.model.response.Message
import com.example.BookShopApp.data.model.OrderDetail
import com.example.BookShopApp.data.model.response.order.OrderList
import com.example.BookShopApp.datasource.IDataSource
import retrofit2.Response

class OrderRepositoryImp(private val dataSource: IDataSource) : OrderRepository {
    override suspend fun getOrderHistory(): Response<OrderList>? {
        return dataSource.getOrderHistory()
    }

    override suspend fun getOrderDetail(orderId: Int): Response<OrderDetail>? {
        return dataSource.getOrderDetail(orderId)
    }

    override suspend fun createOrder(
        cartId: String,
        shippingId: Int,
        receiverId: Int,
        paymentId: Int,
    ): Response<Message> {
        return dataSource.createOrder(
            cartId,
            shippingId,
            receiverId,
            paymentId
        )
    }

    override suspend fun updateOrderStatus(orderId: Int, orderStatusId: Int): Response<Message> {
        return dataSource.updateOrderStatus(orderId, orderStatusId)
    }
}