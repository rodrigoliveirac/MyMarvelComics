package com.rodcollab.mymarvelcomics.core.domain

import androidx.paging.PagingData
import androidx.paging.map
import com.rodcollab.mymarvelcomics.core.data.model.toComic
import com.rodcollab.mymarvelcomics.core.data.repository.ComicsRepository
import com.rodcollab.mymarvelcomics.core.model.Comic
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetComicsByCharIdImpl @Inject constructor(private val comics: ComicsRepository) :
    GetComicsByCharId {
    override fun invoke(charId: Int): Flow<PagingData<Comic>> {
        return comics.getPagingComicsByCharId(15, charId).map { it.map { it.toComic() } }
    }
}

interface GetComicsByCharId {
    operator fun invoke(charId: Int): Flow<PagingData<Comic>>
}