package com.rodcollab.mymarvelcomics.core.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rodcollab.mymarvelcomics.core.network.model.CharacterList
import com.rodcollab.mymarvelcomics.core.network.model.ContentSummary
import org.json.JSONArray
import org.json.JSONObject

class CharacterListTypeConverter {

    @TypeConverter
    fun fromString(value: String?): CharacterList? {
        val gson = Gson()
        return value?.let {
            val jsonObject = gson.fromJson(it, JsonObject::class.java)
            CharacterList(
                available = jsonObject.get("available").asInt,
                collectionURI = jsonObject.get("collectionURI").asString,
                items = jsonObject.get("items").asJsonArray.map { jsonElement ->
                    ContentSummary(
                        resourceURI = jsonElement.asJsonObject.get("resourceURI").asString,
                        name = jsonElement.asJsonObject.get("name").asString
                    )
                }
            )
        }
    }

    @TypeConverter
    fun characterListToString(characterList: CharacterList?): String {
        val jsonObject = JSONObject()
        jsonObject.put("available", characterList?.available)
        jsonObject.put("collectionURI", characterList?.collectionURI)
        characterList?.items
        val array = JSONArray()
        characterList?.items?.map { characterSummary ->
            val characterObject = JSONObject()
            jsonObject.put("resourceURI", characterSummary.resourceURI)
            jsonObject.put("name", characterSummary.name)
            array.put(characterObject)
        }
        jsonObject.put("items", array)
        return jsonObject.toString()
    }
}