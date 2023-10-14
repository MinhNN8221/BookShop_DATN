package com.example.BookShopApp.data.model.response.auth

import android.text.TextUtils
import android.util.Patterns
import com.example.BookShopApp.data.model.Customer
import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("accessToken") val accessToken: String = "",
    @SerializedName("customer") val customer: Customer,
    @SerializedName("expires_in") val expiresIn: String = "",
) {
    fun isSignInFieldEmpty(): Boolean {
        return TextUtils.isEmpty(customer.email) || TextUtils.isEmpty(customer.password)
    }

    fun isUpdateOrderInfor():Boolean{
        return TextUtils.isEmpty(customer.mobPhone) || TextUtils.isEmpty(customer.name) || TextUtils.isEmpty(customer.address)
    }
    fun isSignUpFieldEmpty(): Boolean {
        return TextUtils.isEmpty(customer.email) || TextUtils.isEmpty(customer.password) || TextUtils.isEmpty(
            customer.name
        ) || TextUtils.isEmpty(customer.passwordAgain)
    }

    fun isChangePassEmpty(): Boolean {
        return TextUtils.isEmpty(customer.password) || TextUtils.isEmpty(customer.passwordAgain) || TextUtils.isEmpty(
            customer.newPassword
        )
    }

    fun isForgotPassFieldEmpty(): Boolean {
        return TextUtils.isEmpty(customer.email)
    }

    fun isValidEmail(): Boolean {
        return customer.email.let { Patterns.EMAIL_ADDRESS.matcher(it).matches() }
    }

    fun isPasswordGreaterThan4(): Boolean {
        return customer.password.length >= 5
    }

    fun isPasswordMatch(password: String): Boolean {
        return password == customer.passwordAgain
    }

    fun isValidPhone(): Boolean {
        val pattern = Regex("^0\\d{9}$")
        return pattern.matches(customer.mobPhone)
    }
}
