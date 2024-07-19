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

class ComicsUseCaseImpl (private val comicsRepository: ComicsRepository):
    ComicsUseCase {
    override fun invoke(): Flow<PagingData<Comic>> {
        return comicsRepository.getPagingComics(15).flow.map {
            it.map { comicEntity ->
                withContext(Dispatchers.IO) {
                    val call: suspend () -> Comic = {
                        async {
                            var resultComic: Comic? = null
                            comicsRepository.readFavoriteComic(comicEntity.id) {
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

interface ComicsUseCase {
    operator fun invoke() : Flow<PagingData<Comic>>
}