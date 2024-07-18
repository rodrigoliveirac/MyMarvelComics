package com.rodcollab.mymarvelcomics.core.data.model

import com.rodcollab.mymarvelcomics.core.database.model.CharacterEntity
import com.rodcollab.mymarvelcomics.core.model.CharacterExternal
import com.rodcollab.mymarvelcomics.core.network.model.CharacterNetwork

fun CharacterNetwork.toExternal() =
    CharacterExternal(
        id = id,
        name = name,
        description = description,
        thumbnail = "${thumbnail?.path}.${thumbnail?.extension}",
    )

fun CharacterNetwork.toEntity() =
    CharacterEntity(
        id = id,
        name = name,
        description = description,
        thumbnail = "${thumbnail?.path}.${thumbnail?.extension}",
        resourceURI = resourceURI
    )

fun CharacterEntity.toExternal() =
    CharacterExternal(
        id = id,
        name = name,
        description = description,
        thumbnail = thumbnail,
    )