package com.example.BookShopApp.utils.helperzalopay.HMac

import android.os.Build
import androidx.annotation.RequiresApi
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


class HMacUtil {
    val HMACMD5 = "HmacMD5"
    val HMACSHA1 = "HmacSHA1"
    val HMACSHA256 = "HmacSHA256"
    val HMACSHA512 = "HmacSHA512"
    val UTF8CHARSET = StandardCharsets.UTF_8

    val hMACS = LinkedList(
        listOf(
            "UnSupport",
            "HmacSHA256",
            "HmacMD5",
            "HmacSHA384",
            "HMacSHA1",
            "HmacSHA512"
        )
    )
    private fun hMacEncode(algorithm: String, key: String, data: String): ByteArray? {
        var macGenerator: Mac? = null
        try {
            macGenerator = Mac.getInstance(algorithm)
            val signingKey = SecretKeySpec(key.toByteArray(StandardCharsets.UTF_8), algorithm)
            macGenerator.init(signingKey)
        } catch (_: Exception) {
        }
        if (macGenerator == null) {
            return null
        }
        var dataByte: ByteArray? = null
        try {
            dataByte = data.toByteArray(charset("UTF-8"))
        } catch (_: UnsupportedEncodingException) {
        }
        return macGenerator.doFinal(dataByte)
    }
    @RequiresApi(api = Build.VERSION_CODES.O)
    fun hMacBase64Encode(algorithm: String, key: String, data: String): String? {
        val hmacEncodeBytes = hMacEncode(algorithm, key, data) ?: return null
        return Base64.getEncoder().encodeToString(hmacEncodeBytes)
    }
    fun hMacHexStringEncode(algorithm: String, key: String, data: String): String? {
        val hmacEncodeBytes = hMacEncode(algorithm, key, data) ?: return null
        return HexStringUtil().byteArrayToHexString(hmacEncodeBytes)
    }
}