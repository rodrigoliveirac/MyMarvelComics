package com.rodcollab.mymarvelcomics.core.data.model

import com.rodcollab.mymarvelcomics.core.database.model.ComicEntity
import com.rodcollab.mymarvelcomics.core.database.model.FavoriteComicEntity
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.network.model.ComicNetwork

fun ComicNetwork.toEntity(remoteId: Int) =
    ComicEntity(
        remoteId = remoteId,
        title = title,
        description = description,
        pageCount = pageCount,
        thumbnail = thumbnail,
        characters = characters,
        collections = collections,
        resourceURI = resourceURI,
        images = images
    )

fun ComicNetwork.toComic() =
    Comic(
        id = id,
        title = title,
        description = description,
        pageCount = pageCount,
        thumbnail = thumbnail,
        characters = characters,
        collections = collections,
        resourceURI = resourceURI,
        images = images,
        isFavorite = false
    )

fun FavoriteComicEntity.toComic() =
    Comic(
        id = id,
        title = title,
        description = description,
        pageCount = pageCount,
        thumbnail = thumbnail,
        characters = characters,
        collections = collections,
        resourceURI = resourceURI,
        images = images,
        isFavorite = false
    )

fun Comic.toEntity() =
    FavoriteComicEntity(
        id = id,
        title = title,
        description = description,
        pageCount = pageCount,
        thumbnail = thumbnail,
        characters = characters,
        collections = collections,
        resourceURI = resourceURI,
        images = images
    )