package com.ryan.echo.feature.bookmark

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryan.echo.core.common.util.Resource
import com.ryan.echo.core.domain.usecase.BookmarkArticleUseCase
import com.ryan.echo.core.domain.usecase.GetBookmarkedArticlesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookmarkViewModel @Inject constructor(
    private val getBookmarkedArticlesUseCase: GetBookmarkedArticlesUseCase,
    private val bookmarkArticleUseCase: BookmarkArticleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(BookmarkState())
    val state: StateFlow<BookmarkState> = _state.asStateFlow()

    init {
        loadBookmarks()
    }

    fun onEvent(event: BookmarkEvent) {
        when (event) {
            is BookmarkEvent.LoadBookmarks -> loadBookmarks()
            is BookmarkEvent.BookmarkArticle -> bookmarkArticle(event.articleId, event.isBookmarked)
        }
    }

    private fun loadBookmarks() {
        viewModelScope.launch {
            getBookmarkedArticlesUseCase().collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _state.update { it.copy(
                            bookmarkedArticles = result.data,
                            isLoading = false,
                            error = null
                        )}
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(
                            isLoading = false,
                            error = result.message
                        )}
                    }
                }
            }
        }
    }

    private fun bookmarkArticle(articleId: String, isBookmarked: Boolean) {
        viewModelScope.launch {
            val result = bookmarkArticleUseCase(articleId, isBookmarked)
            if (result is Resource.Error) {
                // Show error notification if needed
            }
            // The bookmarked articles flow will be updated automatically
        }
    }
}