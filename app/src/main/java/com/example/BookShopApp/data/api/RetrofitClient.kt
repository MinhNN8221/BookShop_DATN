package com.example.BookShopApp.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private const val BASE_URL = "https://57f3-2405-4802-4677-d410-d2-bd6f-b21b-758d.ngrok.io/"
    private var accessToken=""

    fun updateAccessToken(token: String) {
        accessToken = token
    }
    private val retrofit: Retrofit by lazy {
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
                .header("user-key", accessToken)
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        })
            .connectTimeout(5, TimeUnit.MINUTES) // Đặt thời gian kết nối tối đa
            .readTimeout(5, TimeUnit.MINUTES)    // Đặt thời gian đọc dữ liệu tối đa
            .writeTimeout(5, TimeUnit.MINUTES)   // Đặt thời gian ghi dữ liệu tối đa
            .build()

        val client = httpClient.build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val apiService: ApiInterface by lazy {
        retrofit.create(ApiInterface::class.java)
    }
}