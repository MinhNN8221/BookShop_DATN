package com.example.BookShopApp.ui.auth.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.BookShopApp.data.model.response.auth.AuthResponse
import com.example.BookShopApp.data.model.response.auth.AuthState
import com.example.BookShopApp.data.model.response.Error
import com.example.BookShopApp.data.model.response.ErrorResponse
import com.example.BookShopApp.data.repository.user.UserRepository
import com.example.BookShopApp.data.repository.user.UserRepositoryImp
import com.example.BookShopApp.datasource.remote.RemoteDataSource
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpViewModel : ViewModel() {
    private var _registerResponse = MutableLiveData<AuthState>()
    val registerResponse: LiveData<AuthState> get() = _registerResponse
    private val authRepository: UserRepository = UserRepositoryImp(RemoteDataSource())

    fun checkFields(user: AuthResponse) {
        if (user.isSignUpFieldEmpty()) {
            _registerResponse.postValue(
                AuthState(
                    Error(message = "Fields cannot be empty!"),
                    null
                )
            )
            return
        }
        if (!user.isValidEmail()) {
            _registerResponse.postValue(
                AuthState(
                    Error(message = "Please enter a valid email address!"),
                    null
                )
            )
            return
        }
        if (!user.isPasswordGreaterThan4()) {
            _registerResponse.postValue(
                AuthState(
                    Error(
                        message =
                        "Password must be greater than 4 characters!"
                    ),
                    null
                )
            )
            return
        }
        if (!user.isPasswordMatch(user.customer.password)) {
            _registerResponse.postValue(AuthState(Error(message = "Passwords don't match!"), null))
            return
        }
        signUp(user)
    }

    private fun signUp(user: AuthResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            val response = authRepository.register(
                user.customer.email,
                user.customer.name,
                user.customer.password
            )
            if (response.isSuccessful == true) {
                _registerResponse.postValue(
                    AuthState(
                        Error(message = "Register Successful!"),
                        response.body()
                    )
                )
            } else {
                val errorBody = response.errorBody()?.string()
                val gson = Gson()
                val errorResponse = gson.fromJson(errorBody, ErrorResponse::class.java)
                _registerResponse.postValue(
                    AuthState(
                        Error(message = errorResponse.error.message),
                        null
                    )
                )
                Log.d("RegisterNull", "NULL")
            }
        }
    }
}