package com.rodcollab.mymarvelcomics.core.network.mockservice

import com.rodcollab.mymarvelcomics.BuildConfig
import com.rodcollab.mymarvelcomics.core.network.AndroidFileReader
import com.rodcollab.mymarvelcomics.core.utils.StatusCode
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody

class MockInterceptor(private val code: Int, private val fileReader: AndroidFileReader, mockContent: String) : Interceptor {

    private var content: String = mockContent

    override fun intercept(chain: Interceptor.Chain): Response {

        var response: Response? = null

        response = if (BuildConfig.DEBUG) {

            val responseBody = when(code) {
                StatusCode.OK.code -> fileReader.getSuccessResponse(content)
                StatusCode.UNAUTHORIZED.code -> StatusCode.UNAUTHORIZED.message
                else -> createGenericErrorResponse()
            }

            Response.Builder()
                .code(code)
                .message(responseBody)
                .request(chain.request())
                .protocol(Protocol.HTTP_1_0)
                .body(ResponseBody.create("application/json".toMediaType(), responseBody.toByteArray()))
                .addHeader("content-type", "application/json")
                .build()
        } else {
            chain.proceed(chain.request())
        }
        return response
    }

    private fun createGenericErrorResponse() = StatusCode.GENERIC.message
}