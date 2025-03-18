package com.ryan.echo.feature.bookmark

sealed class BookmarkEvent {
    data object LoadBookmarks : BookmarkEvent()
    data class BookmarkArticle(val articleId: String, val isBookmarked: Boolean) : BookmarkEvent()
}