package com.ryan.echo.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryan.echo.core.common.util.Resource
import com.ryan.echo.core.domain.usecase.BookmarkArticleUseCase
import com.ryan.echo.core.domain.usecase.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    private val bookmarkArticleUseCase: BookmarkArticleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state: StateFlow<HomeState> = _state.asStateFlow()

    init {
        loadTopHeadlines()
    }

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.LoadTopHeadlines -> loadTopHeadlines()
            is HomeEvent.SelectCategory -> selectCategory(event.category)
            is HomeEvent.BookmarkArticle -> bookmarkArticle(event.articleId, event.isBookmarked)
            is HomeEvent.RefreshTopHeadlines -> refreshTopHeadlines()
        }
    }

    private fun loadTopHeadlines(forceRefresh: Boolean = false) {
        viewModelScope.launch {
            getTopHeadlinesUseCase(
                country = "us",
                category = _state.value.selectedCategory,
                forceRefresh = forceRefresh
            ).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _state.update { it.copy(
                            articles = result.data,
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

    private fun selectCategory(category: String?) {
        _state.update { it.copy(selectedCategory = category) }
        loadTopHeadlines(forceRefresh = true)
    }

    private fun bookmarkArticle(articleId: String, isBookmarked: Boolean) {
        viewModelScope.launch {
            val result = bookmarkArticleUseCase(articleId, isBookmarked)
            if (result is Resource.Error) {
                // Show error notification if needed
            }
        }
    }

    private fun refreshTopHeadlines() {
        loadTopHeadlines(forceRefresh = true)
    }
}