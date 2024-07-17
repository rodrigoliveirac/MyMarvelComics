package com.rodcollab.mymarvelcomics.core.network

import com.rodcollab.mymarvelcomics.core.network.model.CharacterNetwork
import com.rodcollab.mymarvelcomics.core.network.model.ComicNetwork
import com.rodcollab.mymarvelcomics.core.network.model.ResponseContainer
import com.rodcollab.mymarvelcomics.core.network.model.ResponseData
import com.rodcollab.mymarvelcomics.core.network.service.MarvelApi
import retrofit2.Response
import java.io.IOException
import kotlin.math.min

/**
 * implements the MarvelApi with controllable requests
 */
class FakeMarvelApi : MarvelApi {

    private val model = mutableListOf<CharacterNetwork>()

    var failureMsg: String? = null

    fun addCharacter(characterNetwork: CharacterNetwork) {
        model.add(characterNetwork)
    }

    fun clearCharacters() {
        model.clear()
    }

    private fun MutableList<CharacterNetwork>.findCharacters(
        limit: Int,
        offset: Int,
    ): ResponseContainer<CharacterNetwork> {

        failureMsg?.let {
            throw IOException(it)
        }

        if (offset >= size) {
            return ResponseContainer(
                code = 0,
                data = ResponseData(
                    count = 0,
                    limit = limit,
                    offset = offset,
                    results = emptyList(),
                    total = 0
                ),
                status = "ok"
            )
        }
        val endPos = min(size, offset + limit)
        return ResponseContainer(
            code = 0,
            data = ResponseData(
                count = 0,
                limit = limit,
                offset = offset,
                results =  subList(offset, endPos),
                total = 0
            ),
            status = "ok"
        )
    }

    override suspend fun getComics(
        limit: Int,
        offset: Int,
    ): Response<ResponseContainer<ComicNetwork>> {
        return Response.success(null)
    }

    override suspend fun getComicDetails(comicId: Int): Response<ResponseContainer<ComicNetwork>> {
        return Response.success(null)
    }

    override suspend fun getCharacterDetails(characterId: Int): Response<ResponseContainer<CharacterNetwork>> {
        TODO("Not yet implemented")
    }

    override suspend fun getCharacters(
        limit: Int,
        offset: Int,
    ): Response<ResponseContainer<CharacterNetwork>> {
        return Response.success(200,model.findCharacters(limit, offset))
    }
}