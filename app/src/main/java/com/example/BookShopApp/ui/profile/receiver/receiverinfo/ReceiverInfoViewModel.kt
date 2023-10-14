package com.example.BookShopApp.ui.profile.receiver.receiverinfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.response.ErrorResponse
import com.example.BookShopApp.data.model.Receiver
import com.example.BookShopApp.data.repository.user.UserRepository
import com.example.BookShopApp.data.repository.user.UserRepositoryImp
import com.example.BookShopApp.datasource.remote.RemoteDataSource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReceiverInfoViewModel : ViewModel() {
    private val _receiverInfo = MutableLiveData<Receiver>()
    val receiverInfo: LiveData<Receiver> get() = _receiverInfo
    private val _messageAddReceiver = MutableLiveData<String>()
    val messageAddReceiver: LiveData<String> get() = _messageAddReceiver
    private var userRepository: UserRepository? = UserRepositoryImp(RemoteDataSource())

    fun getReceiverInfo(receiverId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRepository?.getReceiverInfo(receiverId)
            if (response?.isSuccessful == true) {
                _receiverInfo.postValue(response.body())
            } else {
                Log.d("getReceiverInfo", "NULL")
            }
        }
    }

    fun addReceiverInfo(receiverName: String, receiverPhone: String, receiverAddress: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                userRepository?.addReceiverInfo(receiverName, receiverPhone, receiverAddress)
            if (response?.isSuccessful == true) {
                _messageAddReceiver.postValue(response.body()?.message)
            } else {
                val errorBody = response?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _messageAddReceiver.postValue(errorResponse.error.message)
            }
        }
    }

    fun getReceiverDefault(){
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRepository?.getReceiverDefault()
            if (response?.isSuccessful == true) {
                _receiverInfo.postValue(response.body())
            } else {
                Log.d("getReceiverInfo", "NULL")
            }
        }
    }

    fun checkFields(receiver: Receiver) {
        if (receiver.isAddReceiverInfo()) {
            _messageAddReceiver.postValue("Các trường không được để trống!")
            return
        }
        if (!receiver.isValidPhone()) {
            _messageAddReceiver.postValue("Hãy nhập đúng định dạng số điện thoại!")
            return
        }
        addReceiverInfo(
            receiver.receiverName,
            receiver.receiverPhone,
            receiver.receiverAddress
        )
    }
}