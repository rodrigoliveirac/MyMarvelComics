package com.rodcollab.mymarvelcomics.core.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.rodcollab.mymarvelcomics.core.network.model.Image
import org.json.JSONArray
import org.json.JSONObject

class ListImageTypeConverter {
    @TypeConverter
    fun fromStringToImageList(value: String): List<Image?>? {
        val gson = Gson()

        return try {
            val jsonArray = gson.fromJson(value, JsonArray::class.java)
            jsonArray.map { item ->
                try {
                    Image(
                        path = item.asJsonObject["path"].asString,
                        extension = item.asJsonObject["extension"].asString
                    )
                } catch (e:Exception) {
                    null
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun imageListToString(images: List<Image?>?): String {
        val jsonArray = JSONArray()
        images?.map { image ->
            image?.let {
                val jsonObject = JSONObject()
                jsonObject.put("path", it.path)
                jsonObject.put("extension", it.extension)
            } ?: run {
                jsonArray.put(JSONObject.NULL)
            }
        }
        return jsonArray.toString()
    }
}