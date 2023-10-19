package com.example.BookShopApp.utils

import android.content.Context
import android.content.SharedPreferences

object MySharedPreferences {
    private const val sharedPreferencesName = "myPreferences"
    private lateinit var sharedPreferences: SharedPreferences
    const val ACCESS_TOKEN = "access_token"
    const val KEY_PERMISSION_DENIED_COUNT = "KEY_PERMISSION_DENIED_COUNT"
    fun init(context: Context) {
        sharedPreferences =
            context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    }

    fun putString(key: String, value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String, defaultValue: String): String {
        return sharedPreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun putLogInTime(key: String, value: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(key, value)
        editor.apply()
    }

    fun getLogInTime(key: String, defaultValue: Long): Long {
        return sharedPreferences.getLong(key, defaultValue)
    }

    fun putAccessToken(value: String) {
        val editor = sharedPreferences.edit()
        editor.putString(ACCESS_TOKEN, value)
        editor.apply()
    }

    fun getAccessToken(defaultValue: String?): String? {
        return sharedPreferences.getString(ACCESS_TOKEN, defaultValue) ?: defaultValue
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun putInt(key: String, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String, defaultValue: Int): Int {
        return sharedPreferences.getInt(key, defaultValue)
    }

    fun clearAccessToken() {
        val editor = sharedPreferences.edit()
        editor.remove(ACCESS_TOKEN)
        editor.apply()
    }

    fun clearPreferences() {
        val editor = sharedPreferences.edit()
        val allEntries: Map<String, *> = sharedPreferences.all
        for ((key, _) in allEntries) {
            if (key != "firstLaunch") {
                editor.remove(key)
            }
        }
        editor.apply()
    }

    fun clearDataCache() {
        val editor = sharedPreferences.edit()
        val allEntries: Map<String, *> = sharedPreferences.all
        for ((key, _) in allEntries) {
            if (key != "firstLaunch" && key != ACCESS_TOKEN && key != "idCustomer") {
                editor.remove(key)
            }
        }
        editor.apply()
    }

    fun putPermissionDeniedCount(count: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(KEY_PERMISSION_DENIED_COUNT, count)
        editor.apply()
    }

    fun getPermissionDeniedCount(): Int {
        return sharedPreferences.getInt(KEY_PERMISSION_DENIED_COUNT, 0)
    }

    fun removePermissionDeniedCount() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_PERMISSION_DENIED_COUNT)
        editor.apply()
    }
}