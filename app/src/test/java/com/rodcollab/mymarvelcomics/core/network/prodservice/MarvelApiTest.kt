package com.rodcollab.mymarvelcomics.core.network.prodservice

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.hamcrest.MatcherAssert.assertThat
import com.rodcollab.mymarvelcomics.BuildConfig
import com.rodcollab.mymarvelcomics.core.utils.md5
import com.rodcollab.mymarvelcomics.core.network.interceptors.AuthInterceptor
import com.rodcollab.mymarvelcomics.core.network.interceptors.UserAgentInterceptor
import com.rodcollab.mymarvelcomics.core.network.model.CharacterNetwork
import com.rodcollab.mymarvelcomics.core.network.model.ComicNetwork
import com.rodcollab.mymarvelcomics.core.network.model.ResponseContainer
import com.rodcollab.mymarvelcomics.core.network.service.MarvelApi
import com.rodcollab.mymarvelcomics.core.network.service.lastPath
import kotlinx.coroutines.test.runTest
import okhttp3.OkHttpClient
import org.hamcrest.core.IsEqual
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * In order to test Real MarvelApi Behaviour
 */

@RunWith(RobolectricTestRunner::class)
class MarvelApiTest  {

    private lateinit var marvelService: MarvelApi
    private var comicsWrapper: ResponseContainer<ComicNetwork>? = null
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
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
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
    fun `When the communication with the service is successful`() = runTest {

        val timestamp = System.currentTimeMillis().toString()

        val apiKey = BuildConfig.PUBLIC_API_KEY

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
             .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
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
    fun `When the communication with the service fails`() = runTest {
        val timestamp = System.currentTimeMillis().toString()

        val apiKey = WRONG_KEY

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
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

    private suspend fun fetchComics() : ResponseContainer<ComicNetwork>? {
        val comics = marvelService.getComics(15, offset = 0)
        return if (comics.isSuccessful) {
            comics.body()
        } else {
            null
        }
    }

    @Test
    fun `When the communication with the service fetch characters and then get their details`() = runTest {
        val timestamp = System.currentTimeMillis().toString()

        val apiKey = BuildConfig.PUBLIC_API_KEY

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(timestamp, apiKey, "$timestamp${BuildConfig.PRIVATE_API_KEY}${apiKey}".md5()))
            .addInterceptor(UserAgentInterceptor())
            .build()

        marvelService = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MarvelApi::class.java)

        val characters = fetchCharacters()

        assertTrue(characters?.data?.results?.size == 15)

        val characterTarget = characters?.data?.results?.get(0)
        val endpointCharacterDetails = characterTarget?.resourceURI!!
        val characterId = endpointCharacterDetails.lastPath()
        val characterNetwork = marvelService.getCharacterDetails(characterId.toInt())
        if(characterNetwork.isSuccessful) {
            assertThat(characterNetwork.body()?.data?.results?.get(0)?.id,
                IsEqual.equalTo(characterTarget.id)
            )
            assertThat(characterNetwork.body()?.data?.results?.get(0)?.name,
                IsEqual.equalTo(characterTarget.name)
            )
            assertThat(characterNetwork.body()?.data?.results?.get(0)?.description,
                IsEqual.equalTo(characterTarget.description)
            )
            assertThat(characterNetwork.body()?.data?.results?.get(0)?.thumbnail,
                IsEqual.equalTo(characterTarget.thumbnail)
            )
            assertThat(characterNetwork.body()?.data?.results?.get(0)?.resourceURI,
                IsEqual.equalTo(characterTarget.resourceURI)
            )
        }
    }

    @Test
    fun `When the communication with the service fetch comics and then get their details`() = runTest {
        val timestamp = System.currentTimeMillis().toString()

        val apiKey = BuildConfig.PUBLIC_API_KEY

        val client = OkHttpClient.Builder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(timestamp, apiKey, "$timestamp${BuildConfig.PRIVATE_API_KEY}${apiKey}".md5()))
            .addInterceptor(UserAgentInterceptor())
            .build()

        marvelService = Retrofit.Builder()
            .baseUrl("https://gateway.marvel.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(MarvelApi::class.java)

        val comics = fetchComics()

        assertTrue(comics?.data?.results?.size == 15)

        val characterTarget = comics?.data?.results?.get(0)
        val endpointCharacterDetails = characterTarget?.resourceURI!!
        val characterId = endpointCharacterDetails.lastPath()
        val characterNetwork = marvelService.getComicDetails(characterId.toInt())

        if(characterNetwork.isSuccessful) {
            assertThat(characterNetwork.body()?.data?.results?.get(0)?.id,
                IsEqual.equalTo(characterTarget.id)
            )
            assertThat(characterNetwork.body()?.data?.results?.get(0)?.title,
                IsEqual.equalTo(characterTarget.title)
            )

            assertThat(characterNetwork.body()?.data?.results?.get(0)?.description,
                IsEqual.equalTo(characterTarget.description)
            )
            assertThat(characterNetwork.body()?.data?.results?.get(0)?.thumbnail,
                IsEqual.equalTo(characterTarget.thumbnail)
            )
            assertThat(characterNetwork.body()?.data?.results?.get(0)?.resourceURI,
                IsEqual.equalTo(characterTarget.resourceURI)
            )
        }
    }

    private suspend fun fetchCharacters() : ResponseContainer<CharacterNetwork>? {
        val characters = marvelService.getCharacters(15, offset = 0)
        return if (characters.isSuccessful) {
            characters.body()
        } else {
            null
        }
    }
    companion object {
        const val WRONG_KEY = "000"
    }
}