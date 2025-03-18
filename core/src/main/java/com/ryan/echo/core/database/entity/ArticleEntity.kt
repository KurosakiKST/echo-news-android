package com.ryan.echo.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.ryan.echo.core.database.converter.SourceConverter
import java.util.UUID

@Entity(tableName = "articles")
@TypeConverters(SourceConverter::class)
data class ArticleEntity(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    val sourceId: String?,
    val sourceName: String,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val content: String?,
    val isBookmarked: Boolean = false,
    val timestamp: Long = System.currentTimeMillis()
)

data class SourceEntity(
    val id: String?,
    val name: String
)