package com.rodcollab.mymarvelcomics.core.network.di

import com.rodcollab.mymarvelcomics.BuildConfig
import com.rodcollab.mymarvelcomics.core.utils.md5
import com.rodcollab.mymarvelcomics.core.network.interceptors.AuthInterceptor
import com.rodcollab.mymarvelcomics.core.network.interceptors.UserAgentInterceptor
import com.rodcollab.mymarvelcomics.core.network.service.MarvelApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
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
    fun provideOkHttpClient(authInterceptor: AuthInterceptor, userAgentInterceptor: UserAgentInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(userAgentInterceptor)
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