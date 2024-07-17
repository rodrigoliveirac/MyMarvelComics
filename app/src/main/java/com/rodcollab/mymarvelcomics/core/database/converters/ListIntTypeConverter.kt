package com.rodcollab.mymarvelcomics.core.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonArray
import org.json.JSONArray
import org.json.JSONObject

class ListIntTypeConverter {
    @TypeConverter
    fun fromStringToIntList(value: String): List<Int>? {
        val gson = Gson()

        return try {
            val jsonArray = gson.fromJson(value, JsonArray::class.java)
            jsonArray.map { item ->
                item.asJsonObject["id"].asInt
            }
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun longListToString(ids: List<Int?>?): String {
        val jsonArray = JSONArray()
        ids?.map { id ->
            id?.let {
                val obj = JSONObject()
                obj.put("id", id)
            } ?: run {
                jsonArray.put(JSONObject.NULL)
            }
        }
        return jsonArray.toString()
    }
}