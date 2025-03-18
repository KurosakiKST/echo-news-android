package com.ryan.echo.feature.home

import com.ryan.echo.core.domain.model.Article

data class HomeState(
    val articles: List<Article> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCategory: String? = null,
    val categories: List<String> = listOf("business", "entertainment", "general", "health", "science", "sports", "technology")
)