package com.example.gitsimpledemo.data.network.api

import android.content.Context
import com.example.gitsimpledemo.Constants
import com.example.gitsimpledemo.data.mock.MockInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Author: Ryu
 * Date: 2024/05/24
 * Description:
 */

object RetrofitManager {

    private const val BASE_URL = Constants.BASE_URL

    private lateinit var retrofit: Retrofit

    fun initialize(context: Context) {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val headerInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(Constants.HEADER_AUTHORIZATION, Constants.HEADER_TOKEN)
                .build()
            chain.proceed(request)
        }

        val mockInterceptor = MockInterceptor(context) // 创建 MockInterceptor 实例

        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(headerInterceptor)
            .addInterceptor(mockInterceptor) // 添加 MockInterceptor
            .connectTimeout(Constants.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(Constants.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(Constants.WRITE_TIMEOUT, TimeUnit.SECONDS)
        if (Constants.USE_MOCK) {
            clientBuilder.addInterceptor(MockInterceptor(context)) // 添加 MockInterceptor
        }

        val client = clientBuilder.build()


        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}


