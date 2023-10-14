package com.example.BookShopApp.data.model.response.auth

import com.example.BookShopApp.data.model.response.Error

data class AuthState(
    val error: Error,
    val loginResponse: AuthResponse?,
)
