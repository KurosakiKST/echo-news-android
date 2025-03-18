package com.ryan.echo.core.domain.usecase

import com.ryan.echo.core.common.util.Resource
import com.ryan.echo.core.domain.model.Article
import com.ryan.echo.core.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBookmarkedArticlesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(): Flow<Resource<List<Article>>> {
        return newsRepository.getBookmarkedArticles()
    }
}