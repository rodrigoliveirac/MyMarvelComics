package com.rodcollab.mymarvelcomics.core.network.mockservice

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@RunWith(RobolectricTestRunner::class)
abstract class WebServiceAbstraction<T> {

    private lateinit var mockWebServer: MockWebServer
    private lateinit var context: Context

    @Before
    fun mockServer() {
        mockWebServer = MockWebServer()
        context = ApplicationProvider.getApplicationContext()
        mockWebServer.start()
    }

    @After
    fun stopServer() {
        mockWebServer.shutdown()
    }

    suspend fun createService(clazz: Class<T>, client: OkHttpClient): T {
        return withContext(Dispatchers.IO) {
            Retrofit.Builder()
                .baseUrl(mockWebServer.url("/"))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
                .create(clazz)
        }
    }
}