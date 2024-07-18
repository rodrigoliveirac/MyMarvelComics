package com.rodcollab.mymarvelcomics.core.data.model

import com.rodcollab.mymarvelcomics.core.database.model.ComicEntity
import com.rodcollab.mymarvelcomics.core.database.model.FavoriteComicEntity
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.network.model.ComicNetwork

fun ComicEntity.toComic(characters: List<CharacterExternal>? = null) =
    Comic(
        id = remoteId,
        title = title,
        description = description,
        pageCount = pageCount,
        thumbnail = thumbnail,
        characters = characters,
        collections = null,
        resourceURI = resourceURI,
        images = null,
        isFavorite = false,
        resourceList = null,
    )

fun ComicNetwork.toEntity(characterIds: List<Int>? = null) =
    ComicEntity(
        remoteId = id,
        title = title,
        description = description,
        pageCount = pageCount,
        thumbnail = thumbnail,
        characters = characterIds,
        resourceURI = resourceURI,
    )

fun ComicNetwork.toComic(characters: List<CharacterExternal>? = null) =
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
        isFavorite = false,
        resourceList = this.characters
    )

fun FavoriteComicEntity.toComic(characters: List<CharacterExternal>? = null) =
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
        isFavorite = false,
        resourceList = this.characters
    )

fun Comic.toEntity() =
    FavoriteComicEntity(
        id = id,
        title = title,
        description = description,
        pageCount = pageCount,
        thumbnail = thumbnail,
        characters = resourceList,
        collections = collections,
        resourceURI = resourceURI,
        images = images
    )