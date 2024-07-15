package com.rodcollab.mymarvelcomics.core.network.mockservice

import com.rodcollab.mymarvelcomics.core.network.model.ComicDataWrapper
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Query

interface DummyService {
    @GET("fake-api")
    suspend fun comics(@Query("offset") offset: Int): Response<ComicDataWrapper>
}