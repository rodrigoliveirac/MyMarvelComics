package com.rodcollab.mymarvelcomics.core.database.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.rodcollab.mymarvelcomics.core.network.model.Thumbnail
import org.json.JSONObject

class ThumbnailTypeConverter {
    @TypeConverter
    fun fromStringToThumbnail(value: String): Thumbnail? {
        val gson = Gson()
        return try {
            val jsonObject = gson.fromJson(value, JsonObject::class.java)
            Thumbnail(
                path = jsonObject.get("path").asString,
                extension = jsonObject.get("extension").asString
            )
        } catch (e: Exception) {
            null
        }
    }

    @TypeConverter
    fun thumbnailToString(contentSummary: Thumbnail?): String {
        val jsonObject = JSONObject()
        jsonObject.put("path", contentSummary?.path)
        jsonObject.put("extension", contentSummary?.extension)
        return jsonObject.toString()
    }
}