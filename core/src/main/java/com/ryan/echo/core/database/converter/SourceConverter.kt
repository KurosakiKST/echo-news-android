package com.ryan.echo.core.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ryan.echo.core.database.entity.SourceEntity

class SourceConverter {
    private val gson = Gson()

    @TypeConverter
    fun fromSource(source: SourceEntity): String {
        return gson.toJson(source)
    }

    @TypeConverter
    fun toSource(sourceString: String): SourceEntity {
        val type = object : TypeToken<SourceEntity>() {}.type
        return gson.fromJson(sourceString, type)
    }
}