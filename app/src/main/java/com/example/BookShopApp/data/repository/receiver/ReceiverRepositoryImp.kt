package com.example.BookShopApp.data.repository.receiver

import com.example.BookShopApp.data.model.Receiver
import com.example.BookShopApp.data.model.response.Message
import com.example.BookShopApp.data.model.response.ReceiverResponse
import com.example.BookShopApp.datasource.IDataSource
import retrofit2.Response

class ReceiverRepositoryImp(private val dataSource: IDataSource) : ReceiverRepository {
    override suspend fun getReceiverInfo(receiverId: Int): Response<Receiver> {
        return dataSource.getReceiverInfo(receiverId)
    }

    override suspend fun addReceiverInfo(
        receiverName: String,
        receiverPhone: String,
        receiverAddress: String,
        isDefault: Int,
    ): Response<Message> {
        return dataSource.addReceiverInfo(receiverName, receiverPhone, receiverAddress, isDefault)
    }

    override suspend fun getReceiverDefault(): Response<Receiver> {
        return dataSource.getReceiverDefault()
    }

    override suspend fun getReceiverSelected(): Response<Receiver> {
        return dataSource.getReceiverSelected()
    }

    override suspend fun updateReceiverInfo(
        receiverName: String,
        receiverPhone: String,
        receiverAddress: String,
        receiverId: Int,
        isDefault: Int,
        isSelected: Int,
    ): Response<Message> {
        return dataSource.updateReceiverInfo(
            receiverName,
            receiverPhone,
            receiverAddress,
            receiverId,
            isDefault,
            isSelected,
        )
    }

    override suspend fun getReceivers(): Response<ReceiverResponse> {
        return dataSource.getReceivers()
    }

    override suspend fun removeReceiver(receiverId: Int): Response<Message> {
        return dataSource.removeReceiver(receiverId)
    }

    override suspend fun updateReceiverDefaultIsSelected(): Response<Message> {
        return dataSource.updateReceiverDefaultIsSelected()
    }
}