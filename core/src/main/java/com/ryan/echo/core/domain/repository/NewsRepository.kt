package com.ryan.echo.core.domain.repository

import com.ryan.echo.core.domain.model.Article
import com.ryan.echo.core.common.util.Resource
import kotlinx.coroutines.flow.Flow

interface NewsRepository {
    fun getTopHeadlines(country: String, category: String? = null, forceRefresh: Boolean = false): Flow<Resource<List<Article>>>

    fun searchNews(query: String): Flow<Resource<List<Article>>>

    fun getArticleById(articleId: String): Flow<Resource<Article>>

    fun getBookmarkedArticles(): Flow<Resource<List<Article>>>

    suspend fun bookmarkArticle(articleId: String, isBookmarked: Boolean): Resource<Unit>

    suspend fun refreshTopHeadlines(country: String, category: String? = null): Resource<List<Article>>
}