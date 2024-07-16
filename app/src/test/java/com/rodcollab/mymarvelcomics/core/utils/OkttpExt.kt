package com.rodcollab.mymarvelcomics.core.utils

import okhttp3.Interceptor
import okhttp3.OkHttpClient

fun getOkHttpClient(interceptor: Interceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(
            interceptor
        )
        .build()
}