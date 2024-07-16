package com.rodcollab.mymarvelcomics.core.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.rodcollab.mymarvelcomics.core.network.model.ContentSummary
import org.json.JSONArray
import org.json.JSONObject

class ListContentSummaryTypeConverter {
    @TypeConverter
    fun fromStringToContentSummaryList(value: String): List<ContentSummary>? {
        val gson = Gson()
        return try {
            val jsonArray = gson.fromJson(value, JsonArray::class.java)
            jsonArray.map { item ->
                ContentSummary(
                    resourceURI = item.asJsonObject["resourceURI"].asString,
                    name = item.asJsonObject["name"].asString
                )
            }
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun contentSummaryListToString(contentSummaryList: List<ContentSummary>?): String {
        val jsonArray = JSONArray()
        contentSummaryList?.map {
            val jsonObject = JSONObject()
            jsonObject.put("resourceURI", it.resourceURI)
            jsonObject.put("name", it.name)
            jsonArray.put(jsonObject)
        }
        return jsonArray.toString()
    }
}