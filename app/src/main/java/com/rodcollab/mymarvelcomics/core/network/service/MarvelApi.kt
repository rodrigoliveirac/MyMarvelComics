package com.rodcollab.mymarvelcomics.core.network.service

import com.rodcollab.mymarvelcomics.core.network.model.ComicDataWrapper
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MarvelApi {

    @GET("v1/public/comics")
    suspend fun getComics(@Query("offset") offset: Int) : Response<ComicDataWrapper>
}