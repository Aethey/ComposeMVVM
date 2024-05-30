package com.example.gitsimpledemo.data.network.api

import android.content.Context
import com.example.gitsimpledemo.AppConfig
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
 * Description: RetrofitManager
 */

object RetrofitManager {

    private const val BASE_URL = AppConfig.BASE_URL

    private lateinit var retrofit: Retrofit

    /**
     * Initializes the Retrofit instance with the provided context.
     * @param context The application context used to access assets and resources.
     */
    fun initialize(context: Context) {
        // Interceptor for logging HTTP request and response data
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        // Interceptor for adding headers to every HTTP request
        val headerInterceptor = Interceptor { chain ->
            val request = chain.request().newBuilder()
                .addHeader(AppConfig.HEADER_AUTHORIZATION, AppConfig.HEADER_TOKEN)
                .build()
            chain.proceed(request)
        }

        // Build OkHttpClient with interceptors and timeouts
        val clientBuilder = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Add logging interceptor
            .addInterceptor(headerInterceptor) // Add header interceptor
            .connectTimeout(AppConfig.CONNECT_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(AppConfig.READ_TIMEOUT, TimeUnit.SECONDS)
            .writeTimeout(AppConfig.WRITE_TIMEOUT, TimeUnit.SECONDS)

        // Conditionally add mock interceptor based on configuration
        if (AppConfig.USE_MOCK) {
            clientBuilder.addInterceptor(MockInterceptor(context))
        }

        val client = clientBuilder.build()

        // Build Retrofit instance with the configured OkHttpClient
        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(JacksonConverterFactory.create())
            .build()
    }

    /**
     * Creates a service instance for the specified service class.
     * @param serviceClass The class of the service to create.
     * @return The created service instance.
     */
    fun <T> createService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}

