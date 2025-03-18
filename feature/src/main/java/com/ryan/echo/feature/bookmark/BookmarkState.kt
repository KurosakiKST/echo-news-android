package com.ryan.echo.feature.bookmark

import com.ryan.echo.core.domain.model.Article

data class BookmarkState(
    val bookmarkedArticles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)