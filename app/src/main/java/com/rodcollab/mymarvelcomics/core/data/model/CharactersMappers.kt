package com.rodcollab.mymarvelcomics.core.data.model

import com.rodcollab.mymarvelcomics.core.database.model.CharacterEntity
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal
import com.rodcollab.mymarvelcomics.core.model.Comic
import com.rodcollab.mymarvelcomics.core.network.model.CharacterNetwork

fun CharacterNetwork.toExternal(comicIds:List<Comic>? = null) =
    CharacterExternal(
        id = id,
        name = name,
        description = description,
        thumbnail = "${thumbnail.path}.${thumbnail.extension}",
        comics = comicIds
    )

fun CharacterNetwork.toEntity(comicIds:List<Int>) =
    CharacterEntity(
        remoteId = id,
        name = name,
        description = description,
        thumbnail = "${thumbnail.path}.${thumbnail.extension}",
        comics = comicIds
    )

fun CharacterEntity.toExternal(comicIds:List<Comic>) =
    CharacterExternal(
        id = remoteId,
        name = name,
        description = description,
        thumbnail = thumbnail,
        comics = comicIds
    )