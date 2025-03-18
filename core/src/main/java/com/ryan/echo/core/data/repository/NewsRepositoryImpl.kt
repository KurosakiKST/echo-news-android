package com.ryan.echo.core.data.repository

import com.ryan.echo.core.BuildConfig
import com.ryan.echo.core.common.util.Resource
import com.ryan.echo.core.data.mapper.toArticle
import com.ryan.echo.core.data.mapper.toArticleEntity
import com.ryan.echo.core.database.dao.ArticleDao
import com.ryan.echo.core.domain.model.Article
import com.ryan.echo.core.domain.repository.NewsRepository
import com.ryan.echo.core.network.api.NewsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val articleDao: ArticleDao
) : NewsRepository {

    override fun getTopHeadlines(
        country: String,
        category: String?,
        forceRefresh: Boolean
    ): Flow<Resource<List<Article>>> = channelFlow {
        send(Resource.Loading)

        // Collect data from the database
        launch {
            articleDao.getArticles().collect { entities ->
                val articles = entities.map { it.toArticle() }
                send(Resource.Success(articles))
            }
        }

        // If force refresh is true, make the API call
        if (forceRefresh) {
            launch {
                try {
                    val apiResponse = newsApiService.getTopHeadlines(
                        country = country,
                        category = category,
                        apiKey = BuildConfig.NEWS_API_KEY
                    )

                    // Map the response to entities and save to DB
                    val articleEntities = apiResponse.articles.map { it.toArticleEntity() }
                    articleDao.deleteNonBookmarkedArticles()
                    articleDao.insertArticles(articleEntities)

                    // No need to emit here as the flow will be updated by Room
                } catch (e: HttpException) {
                    send(Resource.Error("Network error: ${e.message}"))
                } catch (e: IOException) {
                    send(Resource.Error("Network unavailable: ${e.message}"))
                } catch (e: Exception) {
                    send(Resource.Error("An error occurred: ${e.message}"))
                }
            }
        }
    }

    override fun searchNews(query: String): Flow<Resource<List<Article>>> = channelFlow {
        send(Resource.Loading)

        // Search locally first
        launch {
            articleDao.searchArticles(query).collectLatest { entities ->
                val articles = entities.map { it.toArticle() }
                send(Resource.Success(articles))
            }
        }

        // If query is not empty, also search via API
        if (query.isNotBlank()) {
            launch {
                try {
                    val apiResponse = newsApiService.searchNews(
                        query = query,
                        apiKey = BuildConfig.NEWS_API_KEY
                    )

                    // Map the response to entities and save to DB
                    val articleEntities = apiResponse.articles.map { it.toArticleEntity() }
                    articleDao.insertArticles(articleEntities)

                    // No need to emit here as the flow will be updated by Room
                } catch (e: HttpException) {
                    send(Resource.Error("Network error: ${e.message}"))
                } catch (e: IOException) {
                    send(Resource.Error("Network unavailable: ${e.message}"))
                } catch (e: Exception) {
                    send(Resource.Error("An error occurred: ${e.message}"))
                }
            }
        }
    }

    override fun getArticleById(articleId: String): Flow<Resource<Article>> {
        return articleDao.getArticleById(articleId)
            .map { entity ->
                if (entity != null) {
                    Resource.Success(entity.toArticle())
                } else {
                    Resource.Error("Article not found")
                }
            }
            .catch { e ->
                emit(Resource.Error("Error retrieving article: ${e.message}"))
            }
    }

    override fun getBookmarkedArticles(): Flow<Resource<List<Article>>> = flow {
        try {
            articleDao.getBookmarkedArticles().collect { entities ->
                emit(Resource.Success(entities.map { it.toArticle() }))
            }
        } catch (e: Exception) {
            emit(Resource.Error("Error retrieving bookmarks: ${e.message}"))
        }
    }

    override suspend fun bookmarkArticle(articleId: String, isBookmarked: Boolean): Resource<Unit> {
        return try {
            articleDao.updateBookmark(articleId, isBookmarked)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error("Failed to update bookmark: ${e.message}")
        }
    }

    override suspend fun refreshTopHeadlines(
        country: String,
        category: String?
    ): Resource<List<Article>> {
        return try {
            val apiResponse = newsApiService.getTopHeadlines(
                country = country,
                category = category,
                apiKey = BuildConfig.NEWS_API_KEY
            )

            // Map the response to entities and save to DB
            val articleEntities = apiResponse.articles.map { it.toArticleEntity() }
            articleDao.deleteNonBookmarkedArticles()
            articleDao.insertArticles(articleEntities)

            // Map to domain models and return
            Resource.Success(articleEntities.map { it.toArticle() })
        } catch (e: HttpException) {
            Resource.Error("Network error: ${e.message}")
        } catch (e: IOException) {
            Resource.Error("Network unavailable: ${e.message}")
        } catch (e: Exception) {
            Resource.Error("An error occurred: ${e.message}")
        }
    }
}