package com.ryan.echo.feature.detail

import com.ryan.echo.core.domain.model.Article

data class DetailState(
    val article: Article? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)