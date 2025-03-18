package com.ryan.echo.feature.search

import com.ryan.echo.core.domain.model.Article

data class SearchState(
    val query: String = "",
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isSearching: Boolean = false
)