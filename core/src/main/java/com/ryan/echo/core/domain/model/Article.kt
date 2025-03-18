package com.ryan.echo.core.domain.model

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Article(
    val id: String,
    val source: Source,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val imageUrl: String?,
    val publishedAt: ZonedDateTime,
    val content: String?,
    val isBookmarked: Boolean = false
) {
    fun getFormattedDate(): String {
        val formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy", Locale.getDefault())
        return publishedAt.format(formatter)
    }

    fun getFormattedTime(): String {
        val formatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.getDefault())
        return publishedAt.format(formatter)
    }
}

data class Source(
    val id: String?,
    val name: String
)