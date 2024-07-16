package com.rodcollab.mymarvelcomics.core.network.mockservice

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.rodcollab.mymarvelcomics.core.MockServiceContent
import com.rodcollab.mymarvelcomics.core.network.AndroidFileReader
import com.rodcollab.mymarvelcomics.core.network.ApiResponseWrapper
import com.rodcollab.mymarvelcomics.core.utils.StatusCode
import com.rodcollab.mymarvelcomics.core.utils.getOkHttpClient
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.IOException

@RunWith(RobolectricTestRunner::class)
class MockMarvelApiTest : WebServiceAbstraction<DummyService>() {

    private lateinit var marvelService: DummyService
    private lateinit var context: Context

    @Before
    fun setup() = runTest {
        context = ApplicationProvider.getApplicationContext<Context>()
    }

    @Throws(IOException::class)
    @Test
    fun `When ComicDataWrapper comes successful`() = runTest {

        val client = getOkHttpClient(
            interceptor =
            MockInterceptor(
                code = StatusCode.OK.code,
                fileReader = AndroidFileReader(context),
                mockContent = MockServiceContent.COMICS
            )
        )

        val result = createService(DummyService::class.java, client)

        marvelService = result

        val response = marvelService.comics(2)

        val responseBody = ApiResponseWrapper.ApiSuccessResponse(data = response)

        val comicDataWrapper = responseBody.data.body()

        Assert.assertEquals(comicDataWrapper?.code, StatusCode.OK.code)
        Assert.assertEquals(comicDataWrapper?.status, StatusCode.OK.message)

        Assert.assertEquals(comicDataWrapper?.data?.count, 3)
        Assert.assertEquals(comicDataWrapper?.data!!.results[0].id, 82967)

    }


    @Throws(IOException::class)
    @Test
    fun `When Interceptor received an unauthorized response`() = runTest {

        val client = getOkHttpClient(
            interceptor =
            MockInterceptor(
                code = StatusCode.UNAUTHORIZED.code,
                fileReader = AndroidFileReader(context),
                mockContent = MockServiceContent.UNAUTHORIZED
            )
        )

        val result = createService(DummyService::class.java, client)

        marvelService = result

        val response = marvelService.comics(2)

        val responseBody = ApiResponseWrapper.ApiErrorResponse(data = response)

        val errorResponse = responseBody.data

        Assert.assertEquals(errorResponse.code(), StatusCode.UNAUTHORIZED.code)
        Assert.assertEquals(errorResponse.message(), StatusCode.UNAUTHORIZED.message)

    }

}