package com.example.BookShopApp.data.repository.receiver

import com.example.BookShopApp.data.model.Receiver
import com.example.BookShopApp.data.model.response.Message
import com.example.BookShopApp.data.model.response.ReceiverResponse
import retrofit2.Response

interface ReceiverRepository {
    suspend fun getReceiverInfo(receiverId: Int): Response<Receiver>

    suspend fun getReceiverDefault(): Response<Receiver>
    suspend fun getReceiverSelected(): Response<Receiver>

    suspend fun addReceiverInfo(
        receiverName: String,
        receiverPhone: String,
        receiverAddress: String,
        isDefault: Int,
    ): Response<Message>

    suspend fun updateReceiverInfo(
        receiverName: String,
        receiverPhone: String,
        receiverAddress: String,
        receiverId: Int,
        isDefault: Int,
        isSelected: Int,
    ): Response<Message>

    suspend fun getReceivers(): Response<ReceiverResponse>

    suspend fun removeReceiver(receiverId: Int): Response<Message>

    suspend fun updateReceiverDefaultIsSelected(): Response<Message>
}