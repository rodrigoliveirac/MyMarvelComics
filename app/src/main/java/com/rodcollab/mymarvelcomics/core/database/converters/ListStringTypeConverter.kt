package com.rodcollab.mymarvelcomics.core.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonArray
import org.json.JSONArray

class ListStringTypeConverter {
    @TypeConverter
    fun fromStringToStringList(value: String): List<String>? {
        val gson = Gson()
        return try {
            val jsonArray = gson.fromJson(value, JsonArray::class.java)
            jsonArray.map { item ->
                item.asString
            }
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun stringListToString(images: List<String>?): String {
        val jsonArray = JSONArray()
        images?.map {
            jsonArray.put(it)
        }
        return jsonArray.toString()
    }
}