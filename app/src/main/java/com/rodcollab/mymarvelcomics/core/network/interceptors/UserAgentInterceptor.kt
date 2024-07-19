package com.rodcollab.mymarvelcomics.core.network.interceptors

import com.rodcollab.mymarvelcomics.BuildConfig
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class OnlineInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val maxAge = 30 // read from cache for 60 seconds even if there is internet connection
        return response.newBuilder()
            .addHeader("Accept", "*/*")
            .addHeader("If-None-Match", response.headers("ETag")[0])
            .header("ETag", "public, max-age=$maxAge")
            .removeHeader("Pragma")
            .build()
    }
}


class UserAgentInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        return chain.proceed(
            chain.request().newBuilder()
                .addHeader("Accept", "*/*")
                .addHeader("User-Agent", "MyMarvelComics/${BuildConfig.VERSION_CODE}")
                .build()
        )
    }
}