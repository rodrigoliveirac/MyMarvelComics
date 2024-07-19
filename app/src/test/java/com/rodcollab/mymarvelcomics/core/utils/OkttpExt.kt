package com.rodcollab.mymarvelcomics.core.utils

import com.google.common.annotations.VisibleForTesting
import okhttp3.Interceptor
import okhttp3.OkHttpClient

@VisibleForTesting
fun getOkHttpClient(interceptor: Interceptor): OkHttpClient {
    return OkHttpClient.Builder()
        .addInterceptor(
            interceptor
        )
        .build()
}