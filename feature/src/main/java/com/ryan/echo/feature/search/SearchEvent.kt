package com.ryan.echo.feature.search

sealed class SearchEvent {
    data class UpdateQuery(val query: String) : SearchEvent()
    data object PerformSearch : SearchEvent()
    data class BookmarkArticle(val articleId: String, val isBookmarked: Boolean) : SearchEvent()
    data object ClearSearch : SearchEvent()
}