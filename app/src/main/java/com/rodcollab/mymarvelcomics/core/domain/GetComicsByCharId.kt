package com.rodcollab.mymarvelcomics.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.rodcollab.mymarvelcomics.core.data.model.toComic
import com.rodcollab.mymarvelcomics.core.data.repository.ComicsRepository
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.utils.ResultOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class GetComicsByCharIdImpl(private val comics: ComicsRepository) :
    GetComicsByCharId {
    override fun invoke(charId: Int): Flow<PagingData<Comic>> {
        return comics.getPagingComicsByCharId(15, charId).flow.map {
            it.map { comicEntity ->
                withContext(Dispatchers.IO) {
                    val call: suspend () -> Comic = {
                        async {
                            var resultComic: Comic? = null
                            comics.readFavoriteComic(comicEntity.id) {
                                resultComic = when (it) {
                                    is ResultOf.Success -> {
                                        it.value
                                    }

                                    is ResultOf.Failure -> {
                                        comicEntity.toComic()
                                    }
                                }
                            }
                            resultComic!!
                        }.await()
                    }
                    call()
                }
            }
        }
    }
}

interface GetComicsByCharId {
    operator fun invoke(charId: Int): Flow<PagingData<Comic>>
}