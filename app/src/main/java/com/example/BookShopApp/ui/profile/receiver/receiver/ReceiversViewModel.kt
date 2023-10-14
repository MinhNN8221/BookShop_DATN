package com.example.BookShopApp.ui.profile.receiver.receiver

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.Receiver
import com.example.BookShopApp.data.model.response.ReceiverResponse
import com.example.BookShopApp.data.repository.user.UserRepository
import com.example.BookShopApp.data.repository.user.UserRepositoryImp
import com.example.BookShopApp.datasource.remote.RemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ReceiversViewModel : ViewModel() {
    private val _receivers = MutableLiveData<List<Receiver>>()
    val receivers: LiveData<List<Receiver>> get() = _receivers
    private var userRepository: UserRepository? = UserRepositoryImp(RemoteDataSource())
    fun getReceivers() {
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRepository?.getReceivers()
            if (response?.isSuccessful == true) {
                _receivers.postValue(response.body()?.receivers)
            } else {
                Log.d("GetReceivers", "NULL")
            }
        }
    }
}