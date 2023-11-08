package com.example.BookShopApp.ui.profile.receiver.receiverinfo

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.response.ErrorResponse
import com.example.BookShopApp.data.model.Receiver
import com.example.BookShopApp.data.model.response.Message
import com.example.BookShopApp.data.repository.receiver.ReceiverRepository
import com.example.BookShopApp.data.repository.receiver.ReceiverRepositoryImp
import com.example.BookShopApp.data.repository.user.UserRepository
import com.example.BookShopApp.data.repository.user.UserRepositoryImp
import com.example.BookShopApp.datasource.remote.RemoteDataSource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ReceiverInfoViewModel : ViewModel() {
    private val _receiverInfo = MutableLiveData<Receiver>()
    val receiverInfo: LiveData<Receiver> get() = _receiverInfo
    private val _messageAddReceiver = MutableLiveData<String>()
    val messageAddReceiver: LiveData<String> get() = _messageAddReceiver
    private val _message = MutableLiveData<Message>()
    val message: LiveData<Message> get() = _message
    var job: Job? = null
    private var receiverRepository: ReceiverRepository? = ReceiverRepositoryImp(RemoteDataSource())

//    fun getReceiverInfo(receiverId: Int) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val response = receiverRepository?.getReceiverInfo(receiverId)
//            if (response?.isSuccessful == true) {
//                _receiverInfo.postValue(response.body())
//            } else {
//                Log.d("getReceiverInfo", "NULL")
//            }
//        }
//    }

    fun addReceiverInfo(
        receiverName: String,
        receiverPhone: String,
        receiverAddress: String,
        isDefault: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                receiverRepository?.addReceiverInfo(
                    receiverName,
                    receiverPhone,
                    receiverAddress,
                    isDefault
                )
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

    fun updateReceiverInfo(
        receiverName: String,
        receiverPhone: String,
        receiverAddress: String,
        receiverId: Int,
        isDefault: Int,
        isSelected: Int,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response =
                receiverRepository?.updateReceiverInfo(
                    receiverName,
                    receiverPhone,
                    receiverAddress,
                    receiverId,
                    isDefault,
                    isSelected
                )
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

    fun getReceiverDefault() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            val response = receiverRepository?.getReceiverDefault()
            if (response?.isSuccessful == true) {
                _receiverInfo.postValue(response.body())
            } else {
                Log.d("getReceiverDefault", "NULL")
            }
        }
    }

    fun getReceiverSelected() {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.IO) {
            val response = receiverRepository?.getReceiverSelected()
            if (response?.isSuccessful == true) {
                _receiverInfo.postValue(response.body())
            } else {
                Log.d("getReceiverSelected", "NULL")
            }
        }
    }

    fun updateReceiverDefaultIsSelected() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = receiverRepository?.updateReceiverDefaultIsSelected()
            if (response?.isSuccessful == true) {
                _message.postValue(response.body())
            } else {
                Log.d("updateReceiverDefaultIsSelected", "NULL")
            }
        }
    }

    fun checkFields(receiver: Receiver, isUpdate: Boolean) {
        if (receiver.isAddReceiverInfo()) {
            _messageAddReceiver.postValue("Các trường không được để trống!")
            return
        }
        if (!receiver.isValidPhone()) {
            _messageAddReceiver.postValue("Hãy nhập đúng định dạng số điện thoại!")
            return
        }
        if (isUpdate) {
            updateReceiverInfo(
                receiver.receiverName,
                receiver.receiverPhone,
                receiver.receiverAddress,
                receiver.receiverId!!,
                receiver.isDefault!!,
                receiver.isSelected!!
            )
        } else {
            addReceiverInfo(
                receiver.receiverName,
                receiver.receiverPhone,
                receiver.receiverAddress,
                receiver.isDefault!!
            )
        }
    }
}