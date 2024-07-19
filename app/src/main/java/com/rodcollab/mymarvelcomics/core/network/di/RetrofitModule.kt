package com.rodcollab.mymarvelcomics.core.network.di

import android.content.Context
import com.rodcollab.mymarvelcomics.BuildConfig
import com.rodcollab.mymarvelcomics.core.network.interceptors.AuthInterceptor
import com.rodcollab.mymarvelcomics.core.network.interceptors.OnlineInterceptor
import com.rodcollab.mymarvelcomics.core.network.interceptors.UserAgentInterceptor
import com.rodcollab.mymarvelcomics.core.network.service.MarvelApi
import com.rodcollab.mymarvelcomics.core.utils.md5
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    private const val BASE_URL = "https://gateway.marvel.com/"

    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor {
        val timestamp = System.currentTimeMillis().toString()
        val apiKey = BuildConfig.PUBLIC_API_KEY
        return AuthInterceptor(timestamp, apiKey, "$timestamp${BuildConfig.PRIVATE_API_KEY}${apiKey}".md5())
    }
    @Provides
    @Singleton
    fun provideUserAgentInterceptor(): UserAgentInterceptor {
        return UserAgentInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext appContext: Context,
        authInterceptor: AuthInterceptor, userAgentInterceptor: UserAgentInterceptor): OkHttpClient {

        val cacheSize = 10 * 1024 * 1024 // 10 MB
        val cache = Cache(appContext.cacheDir, cacheSize.toLong())

        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60,TimeUnit.SECONDS)
            .addInterceptor(authInterceptor)
            .addInterceptor(userAgentInterceptor)
            .addNetworkInterceptor(OnlineInterceptor())
            .cache(cache)
            .build()
    }

    @Provides
    @Singleton
    fun provideMarvelApi(okHttpClient: OkHttpClient): MarvelApi {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MarvelApi::class.java)
    }
}