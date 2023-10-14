package com.example.BookShopApp.ui.profile.updateprofile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.*
import com.example.BookShopApp.data.model.response.ErrorResponse
import com.example.BookShopApp.data.repository.user.UserRepository
import com.example.BookShopApp.data.repository.user.UserRepositoryImp
import com.example.BookShopApp.datasource.remote.RemoteDataSource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class UpdateProfileViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _profile = MutableLiveData<Customer>()
    val profile: LiveData<Customer> get() = _profile
    private var userRepository: UserRepository? = UserRepositoryImp(RemoteDataSource())
    private val _message = MutableLiveData<String>()
    val message: LiveData<String> get() = _message
    fun getCustomer() {

        viewModelScope.launch(Dispatchers.IO) {
            val response = userRepository?.getCustomer()
            if (response?.isSuccessful == true) {
                _profile.postValue(response.body())
            } else {
                Log.d("getProfile", "NULLLL")
            }
        }
    }

    fun updateCustomer(
        name: String,
        address: String,
        dob: String,
        gender: String,
        mob_phone: String,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRepository?.updateCustomer(name, address, dob, gender, mob_phone)
            if (response?.isSuccessful == true) {
                _message.postValue("Update Successful!")
            } else {
                val errorBody = response?.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _message.postValue(errorResponse.error.message)
            }
        }
    }

//    fun checkFields(user: AuthResponse) {
//        if (user.isUpdateOrderInfor()) {
//            _message.postValue("Fields cannot be empty!")
//            return
//        }
//        if (!user.isValidPhone()) {
//            _message.postValue("Please enter the correct format of the phone number!")
//            return
//        }
//        updateOrderInfor(user)
//    }

//    fun updateOrderInfor(user: AuthResponse) {
//        viewModelScope.launch(Dispatchers.IO) {
//            val response = userRepository?.updateOrderInfor(
//                user.customer.name,
//                user.customer.address,
//                user.customer.mobPhone
//            )
//            if (response?.isSuccessful == true) {
//                _message.postValue("Update Successful!")
//            } else {
//                val errorBody = response?.errorBody()?.string()
//                val gson = Gson()
//                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
//                _message.postValue(errorResponse.error.message)
//            }
//        }
//    }

    fun changeAvatar(image: MultipartBody.Part) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRepository?.changeAvatar(image)
            if (response?.isSuccessful == true) {
                _profile.postValue(response.body())
            } else {
                Log.d("ChangeAvatar", "FAILLL")
            }
        }
    }
}