package com.example.gitsimpledemo.data.mock
import android.content.Context
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody

/**
 * Author: Ryu
 * Date: 2024/05/25
 * Description:
 */

class MockInterceptor(private val context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val url = request.url.toString()

        // Determine which mock file to use based on the URL
        val mockFileName = when {
            url.contains("/users") -> "userList.json"
            url.contains("/users/") -> "userDetail.json"
            else -> null
        }

        return if (mockFileName != null) {
            val json = context.assets.open(mockFileName).bufferedReader().use { it.readText() }
            print(json)

            Response.Builder()
                .code(200)
                .message(json)
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .body(ResponseBody.create("application/json".toMediaTypeOrNull(), json))
                .addHeader("content-type", "application/json")
                .build()
        } else {
            chain.proceed(request)
        }
    }
}