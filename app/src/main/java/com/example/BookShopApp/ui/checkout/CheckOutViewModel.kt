package com.example.BookShopApp.ui.checkout

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.response.ErrorResponse
import com.example.BookShopApp.data.model.response.Message
import com.example.BookShopApp.data.model.response.MessageState
import com.example.BookShopApp.data.repository.order.OrderRepository
import com.example.BookShopApp.data.repository.order.OrderRepositoryImp
import com.example.BookShopApp.datasource.remote.RemoteDataSource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckOutViewModel : ViewModel() {
    private var _message = MutableLiveData<MessageState>()
    val message: LiveData<MessageState> get() = _message
    private val orderRepository: OrderRepository = OrderRepositoryImp(RemoteDataSource())

    fun createOrder(
        cartId: String,
        shippingId: Int,
        receiverId: Int,
        paymentId: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                orderRepository.createOrder(
                    cartId,
                    shippingId,
                    receiverId,
                    paymentId,
                )
            if (response.isSuccessful == true) {
                _message.postValue(response.body()?.let { MessageState(true, it) })
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _message.postValue(MessageState(false, Message(errorResponse.error.message)))
            }
        }
    }
}