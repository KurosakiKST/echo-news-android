package com.ryan.echo.core.data.mapper

import com.ryan.echo.core.database.entity.ArticleEntity
import com.ryan.echo.core.database.entity.SourceEntity
import com.ryan.echo.core.domain.model.Article
import com.ryan.echo.core.domain.model.Source
import com.ryan.echo.core.network.model.ArticleDto
import com.ryan.echo.core.network.model.SourceDto
import java.time.ZonedDateTime
import java.util.UUID

// Convert from Domain to Database Entity
fun Article.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        id = id,
        sourceId = source.id,
        sourceName = source.name,
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = imageUrl,
        publishedAt = publishedAt.toString(),
        content = content,
        isBookmarked = isBookmarked
    )
}

// Convert from Database Entity to Domain
fun ArticleEntity.toArticle(): Article {
    return Article(
        id = id,
        source = Source(
            id = sourceId,
            name = sourceName
        ),
        author = author,
        title = title,
        description = description,
        url = url,
        imageUrl = urlToImage,
        publishedAt = try {
            ZonedDateTime.parse(publishedAt)
        } catch (e: Exception) {
            ZonedDateTime.now()
        },
        content = content,
        isBookmarked = isBookmarked
    )
}

// Convert from Network DTO to Database Entity
fun ArticleDto.toArticleEntity(): ArticleEntity {
    return ArticleEntity(
        id = UUID.randomUUID().toString(),
        sourceId = source.id,
        sourceName = source.name,
        author = author,
        title = title,
        description = description,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        content = content,
        isBookmarked = false
    )
}

// Convert from Network DTO to Domain
fun ArticleDto.toArticle(): Article {
    return Article(
        id = UUID.randomUUID().toString(),
        source = Source(
            id = source.id,
            name = source.name
        ),
        author = author,
        title = title,
        description = description,
        url = url,
        imageUrl = urlToImage,
        publishedAt = try {
            ZonedDateTime.parse(publishedAt)
        } catch (e: Exception) {
            ZonedDateTime.now()
        },
        content = content,
        isBookmarked = false
    )
}