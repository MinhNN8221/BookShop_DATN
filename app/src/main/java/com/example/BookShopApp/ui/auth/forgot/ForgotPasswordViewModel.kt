package com.example.BookShopApp.ui.auth.forgot

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.response.auth.AuthResponse
import com.example.BookShopApp.data.model.response.ErrorResponse
import com.example.BookShopApp.data.model.response.Message
import com.example.BookShopApp.data.repository.user.UserRepository
import com.example.BookShopApp.data.repository.user.UserRepositoryImp
import com.example.BookShopApp.datasource.remote.RemoteDataSource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ForgotPasswordViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private var _message = MutableLiveData<Message>()
    val message: LiveData<Message> get() = _message
    private var authRepository: UserRepository = UserRepositoryImp(RemoteDataSource())

    fun checkFields(user: AuthResponse) {
        if (user.isForgotPassFieldEmpty()) {
            _message.postValue(Message("Fields cannot be empty!"))
            return
        }

        if (!user.isValidEmail()) {
            _message.postValue(Message("Please enter a valid email address!"))
            return
        }
        forgotPassword(user.customer.email)
    }
    fun forgotPassword(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = authRepository.forgotPassword(email)
            if (response.isSuccessful==true) {
                _message.postValue(response.body())
            } else {
                val errorBody = response?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _message.postValue(Message(message = errorResponse.error.message))
                Log.d("ForgotPassNull", "NULL")
            }
        }
    }
}