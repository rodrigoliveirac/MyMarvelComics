package com.rodcollab.mymarvelcomics.core.network.interceptors

import com.rodcollab.mymarvelcomics.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class UserAgentInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("User-Agent", "MyMarvelComics/${BuildConfig.VERSION_CODE}")
                .build()
        )
    }
}