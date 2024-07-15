package com.rodcollab.mymarvelcomics.core.data.model

import com.rodcollab.mymarvelcomics.core.database.model.FavoriteComicEntity
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.network.model.ComicNetwork

fun ComicNetwork.toComic() =
    Comic(
        id = id,
        title = title,
        description = description,
        pageCount = pageCount,
        thumbnail = urlImage,
        characters = characters,
        collections = collections,
        resourceURI = resourceURI,
        images = urlImages
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
        images = images
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