package com.rodcollab.mymarvelcomics.core.network.prodservice

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.rodcollab.mymarvelcomics.BuildConfig
import com.rodcollab.mymarvelcomics.core.md5
import com.rodcollab.mymarvelcomics.core.network.interceptors.AuthInterceptor
import com.rodcollab.mymarvelcomics.core.network.interceptors.UserAgentInterceptor
import com.rodcollab.mymarvelcomics.core.network.model.ComicDataWrapper
import com.rodcollab.mymarvelcomics.core.network.service.MarvelApi
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * In order to test Real MarvelApi Behaviour
 */

@RunWith(RobolectricTestRunner::class)
class MarvelApiTest  {

    private lateinit var marvelService: MarvelApi
    private var comicsWrapper: ComicDataWrapper? = null
    private lateinit var context: Context
    @Before
    fun setup()  {

        context = ApplicationProvider.getApplicationContext<Context>()
        marvelService = createMarvelService()

    }
    private fun createMarvelService() : MarvelApi {

        val timestamp = System.currentTimeMillis().toString()
        val apiKey = BuildConfig.PUBLIC_API_KEY

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(timestamp, apiKey, "$timestamp${BuildConfig.PRIVATE_API_KEY}${apiKey}".md5()))
            .addInterceptor(UserAgentInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MarvelApi::class.java)
    }


    @Test
    fun `When the communication with the service is successfully`() = runTest {

        val timestamp = System.currentTimeMillis().toString()

        val apiKey = BuildConfig.PUBLIC_API_KEY

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(timestamp, apiKey, "$timestamp${BuildConfig.PRIVATE_API_KEY}${apiKey}".md5()))
            .addInterceptor(UserAgentInterceptor())
            .build()

        marvelService = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MarvelApi::class.java)

        comicsWrapper = fetchComics()

        assertTrue(comicsWrapper != null)
    }

    @Test
    fun `When the communication with the service got fail`() = runTest {
        val timestamp = System.currentTimeMillis().toString()

        val apiKey = WRONG_KEY

        val client = OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(timestamp, apiKey, "$timestamp${BuildConfig.PRIVATE_API_KEY}${apiKey}".md5()))
            .addInterceptor(UserAgentInterceptor())
            .build()

        marvelService = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MarvelApi::class.java)

        comicsWrapper = fetchComics()

        assertTrue(comicsWrapper == null)
    }

    private suspend fun fetchComics() : ComicDataWrapper? {
        val comics = marvelService.getComics(2)
        return if (comics.isSuccessful) {
            comics.body()
        } else {
            null
        }
    }
    companion object {
        const val WRONG_KEY = "000"
    }
}