package com.rodcollab.mymarvelcomics.core.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rodcollab.mymarvelcomics.core.network.model.ContentSummary
import org.json.JSONObject

class ContentSummaryTypeConverter {
    @TypeConverter
    fun fromStringToContentSummary(value: String): ContentSummary? {
        val gson = Gson()
        return try {
            val jsonObject = gson.fromJson(value, JsonObject::class.java)
            ContentSummary(
                resourceURI = jsonObject.get("resourceURI").asString,
                name = jsonObject.get("name").asString
            )
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun contentSummaryToString(contentSummary: ContentSummary?): String {
        val jsonObject = JSONObject()
        jsonObject.put("resourceURI", contentSummary?.resourceURI)
        jsonObject.put("name", contentSummary?.name)
        return jsonObject.toString()
    }
}