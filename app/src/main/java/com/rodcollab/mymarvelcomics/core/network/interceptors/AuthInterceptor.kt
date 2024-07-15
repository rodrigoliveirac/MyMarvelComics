package com.rodcollab.mymarvelcomics.core.network.interceptors

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val timestamp: String, private val apiKey: String, private val hash: String) :
    Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {

        val originalRequest = chain.request()
        val url = originalRequest.url()
            .newBuilder()
            .addQueryParameter("ts", timestamp)
            .addQueryParameter("apikey", apiKey)
            .addQueryParameter("hash", hash)
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(url)
            .build()
        return chain.proceed(newRequest)
    }
}