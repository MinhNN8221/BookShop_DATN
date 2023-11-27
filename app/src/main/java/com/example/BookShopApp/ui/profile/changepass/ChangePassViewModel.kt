package com.example.BookShopApp.ui.profile.changepass

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.response.auth.AuthResponse
import com.example.BookShopApp.data.model.response.ErrorResponse
import com.example.BookShopApp.data.repository.user.UserRepository
import com.example.BookShopApp.data.repository.user.UserRepositoryImp
import com.example.BookShopApp.datasource.remote.RemoteDataSource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChangePassViewModel : ViewModel() {
    private val _message = MutableLiveData<String>()
    val message: MutableLiveData<String> get() = _message
    private var userRepository: UserRepository? = UserRepositoryImp(RemoteDataSource())

    fun checkFields(user: AuthResponse) {
        if (user.isChangePassEmpty()) {
            _message.postValue("Các trường không được để trống!")
            return
        }
        if (!user.isPasswordGreaterThan5(user.customer.password) || !user.isPasswordGreaterThan5(user.customer.newPassword)) {
            _message.postValue("Mật khẩu phải lớn hơn 5 và phải bao gồm cả chữ và số!")
            return
        }
        if (!user.isPasswordMatch(user.customer.newPassword)) {
            _message.postValue("Mật khẩu không khớp!")
            return
        }
        changePassword(user)
    }

    fun changePassword(user: AuthResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRepository?.changePassword(
                user.customer.email,
                user.customer.password,
                user.customer.newPassword
            )
            if (response?.isSuccessful == true) {
                message.postValue("Thay đổi mật khẩu thành công")
            } else {
                val errorBody = response?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                message.postValue(errorResponse.error.message)
            }
        }
    }
}