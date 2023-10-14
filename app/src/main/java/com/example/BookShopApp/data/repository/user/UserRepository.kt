package com.example.BookShopApp.data.repository.user

import com.example.BookShopApp.data.model.response.auth.AuthResponse
import com.example.BookShopApp.data.model.Customer
import com.example.BookShopApp.data.model.response.Message
import com.example.BookShopApp.data.model.Receiver
import com.example.BookShopApp.data.model.response.ReceiverResponse
import okhttp3.MultipartBody
import retrofit2.Response

interface UserRepository {
    suspend fun login(email: String, password: String): Response<AuthResponse>?
    suspend fun forgotPassword(email: String): Response<Message>
    suspend fun register(email: String, name: String, password: String): Response<AuthResponse>
    suspend fun getCustomer(): Response<Customer>?
    suspend fun updateCustomer(
        name: String,
        address: String,
        dob: String,
        gender: String,
        mob_phone: String,
    ): Response<Customer>?

    suspend fun updateOrderInfor(
        name: String,
        address: String,
        mob_phone: String,
    ): Response<Customer>?

    suspend fun changePassword(
        email: String, old_password: String,
        new_password: String,
    ): Response<Customer>?

    suspend fun changeAvatar(image: MultipartBody.Part): Response<Customer>?

    suspend fun getReceiverInfo(receiverId: Int): Response<Receiver>

    suspend fun getReceiverDefault(): Response<Receiver>

    suspend fun addReceiverInfo(
        receiverName: String,
        receiverPhone: String,
        receiverAddress: String,
    ): Response<Message>

    suspend fun getReceivers(): Response<ReceiverResponse>
}