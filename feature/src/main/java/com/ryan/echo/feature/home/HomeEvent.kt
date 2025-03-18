package com.ryan.echo.feature.home

sealed class HomeEvent {
    data object LoadTopHeadlines : HomeEvent()
    data class SelectCategory(val category: String?) : HomeEvent()
    data class BookmarkArticle(val articleId: String, val isBookmarked: Boolean) : HomeEvent()
    data object RefreshTopHeadlines : HomeEvent()
}