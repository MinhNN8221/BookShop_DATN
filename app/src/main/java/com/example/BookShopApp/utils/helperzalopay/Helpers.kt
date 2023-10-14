package com.example.BookShopApp.utils.helperzalopay

import android.annotation.SuppressLint
import com.example.BookShopApp.utils.helperzalopay.HMac.HMacUtil
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import java.text.SimpleDateFormat
import java.util.*

class Helpers {
    var transIdDefault = 1

    @SuppressLint("DefaultLocale")
    fun getAppTransId(): String {
        if (transIdDefault >= 100000) {
            transIdDefault = 1
        }
        transIdDefault += 1
        @SuppressLint("SimpleDateFormat") val formatDateTime = SimpleDateFormat("yyMMdd_hhmmss")
        val timeString = formatDateTime.format(Date())
        return String.format("%s%06d", timeString, transIdDefault)
    }

    fun getMac(key: String, data: String): String? {
        return HMacUtil().hMacHexStringEncode(HMacUtil().HMACSHA256, key, data)
    }
}