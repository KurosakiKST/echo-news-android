package com.ryan.echo.core.domain.usecase

import com.ryan.echo.core.common.util.Resource
import com.ryan.echo.core.domain.repository.NewsRepository
import javax.inject.Inject

class BookmarkArticleUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(articleId: String, isBookmarked: Boolean): Resource<Unit> {
        return newsRepository.bookmarkArticle(articleId, isBookmarked)
    }
}