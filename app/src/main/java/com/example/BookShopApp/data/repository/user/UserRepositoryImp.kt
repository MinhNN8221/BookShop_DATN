package com.example.BookShopApp.data.repository.user

import com.example.BookShopApp.data.model.response.auth.AuthResponse
import com.example.BookShopApp.data.model.Customer
import com.example.BookShopApp.data.model.response.Message
import com.example.BookShopApp.data.model.Receiver
import com.example.BookShopApp.data.model.response.ReceiverResponse
import com.example.BookShopApp.datasource.IDataSource
import okhttp3.MultipartBody
import retrofit2.Response

class UserRepositoryImp(private val dataSource: IDataSource) : UserRepository {
    override suspend fun login(email: String, password: String): Response<AuthResponse>? {
        return dataSource.login(email, password)
    }

    override suspend fun forgotPassword(email: String): Response<Message> {
        return dataSource.forgotPassword(email)
    }

    override suspend fun register(
        email: String,
        name: String,
        password: String,
    ): Response<AuthResponse> {
        return dataSource.register(email, name, password)
    }

    override suspend fun getCustomer(): Response<Customer>? {
        return dataSource.getCustomer()
    }

    override suspend fun updateCustomer(
        name: String,
        address: String,
        dob: String,
        gender: String,
        mob_phone: String,
    ): Response<Customer>? {
        return dataSource.updateCustomer(name, address, dob, gender, mob_phone)
    }

    override suspend fun updateOrderInfor(
        name: String,
        address: String,
        mob_phone: String,
    ): Response<Customer>? {
        return dataSource.updateOrderInfor(name, address, mob_phone)
    }

    override suspend fun changePassword(
        email: String,
        old_password: String,
        new_password: String,
    ): Response<Customer>? {
        return dataSource.changePassword(email, old_password, new_password)
    }

    override suspend fun changeAvatar(image: MultipartBody.Part): Response<Customer>? {
        return dataSource.changeAvatar(image)
    }
}