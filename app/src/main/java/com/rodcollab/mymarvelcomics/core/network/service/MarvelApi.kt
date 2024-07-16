package com.rodcollab.mymarvelcomics.core.network.service

import com.rodcollab.mymarvelcomics.core.network.model.CharacterNetwork
import com.rodcollab.mymarvelcomics.core.network.model.ComicNetwork
import com.rodcollab.mymarvelcomics.core.network.model.ResponseContainer
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MarvelApi {

    @GET("v1/public/comics")
    suspend fun getComics(@Query("limit") limit: Int, @Query("offset") offset: Int) : Response<ResponseContainer<ComicNetwork>>


    @GET("v1/public/comics/{comicId}")
    suspend fun getComicDetails(@Path("comicId") comicId: Int) : Response<ResponseContainer<ComicNetwork>>

    @GET("v1/public/characters")
    suspend fun getCharacters(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
    ): Response<ResponseContainer<CharacterNetwork>>
}



fun String.lastPath(): String  {
    val segments = split("/".toRegex()).dropLastWhile { it.isEmpty() }
        .toTypedArray()
    return segments[segments.size - 1]
}