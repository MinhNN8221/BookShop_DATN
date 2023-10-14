package com.example.BookShopApp.data.api

import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.ConnectionSpec
import okhttp3.TlsVersion
import okhttp3.CipherSuite
import okhttp3.ResponseBody

class HttpProvider {
    fun sendPost(URL: String, formBody: RequestBody): JSONObject? {
        var data: JSONObject? = null
        try {
            val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                .tlsVersions(TlsVersion.TLS_1_2)
                .cipherSuites(
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_DHE_RSA_WITH_AES_128_GCM_SHA256
                )
                .build()

            val client = OkHttpClient.Builder()
                .connectionSpecs(listOf(spec))
                .callTimeout(5000, TimeUnit.MILLISECONDS)
                .build()

            val request = Request.Builder()
                .url(URL)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .post(formBody)
                .build()

            val response = client.newCall(request).execute()

            if (!response.isSuccessful) {
                val responseBody: ResponseBody? = response.body
                responseBody?.let {
                    val errorMessage = it.string()
                    it.close()
                    println("BAD_REQUEST: $errorMessage")
                }
            } else {
                val responseBody: ResponseBody? = response.body
                responseBody?.let {
                    val responseString = it.string()
                    it.close()
                    data = JSONObject(responseString)
                }
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        return data
    }
}
