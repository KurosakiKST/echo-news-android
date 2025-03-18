package com.ryan.echo.feature.detail

sealed class DetailEvent {
    data class LoadArticle(val articleId: String) : DetailEvent()
    data class BookmarkArticle(val isBookmarked: Boolean) : DetailEvent()
    data object ShareArticle : DetailEvent()
    data object OpenInBrowser : DetailEvent()
}