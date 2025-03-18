package com.ryan.echo.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryan.echo.core.common.util.Resource
import com.ryan.echo.core.domain.usecase.BookmarkArticleUseCase
import com.ryan.echo.core.domain.usecase.SearchNewsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchNewsUseCase: SearchNewsUseCase,
    private val bookmarkArticleUseCase: BookmarkArticleUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private var searchJob: Job? = null

    fun onEvent(event: SearchEvent) {
        when (event) {
            is SearchEvent.UpdateQuery -> updateQuery(event.query)
            is SearchEvent.PerformSearch -> performSearch()
            is SearchEvent.BookmarkArticle -> bookmarkArticle(event.articleId, event.isBookmarked)
            is SearchEvent.ClearSearch -> clearSearch()
        }
    }

    private fun updateQuery(query: String) {
        _state.update { it.copy(query = query) }
    }

    private fun performSearch() {
        if (_state.value.query.isBlank()) {
            return
        }

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            _state.update { it.copy(isSearching = true, isLoading = true, error = null) }

            searchNewsUseCase(_state.value.query).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _state.update { it.copy(
                            articles = result.data,
                            isLoading = false,
                            error = null,
                            isSearching = false
                        )}
                    }
                    is Resource.Error -> {
                        _state.update { it.copy(
                            isLoading = false,
                            error = result.message,
                            isSearching = false
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
                // Handle error if needed
            }
        }
    }

    private fun clearSearch() {
        searchJob?.cancel()
        _state.update {
            SearchState()
        }
    }
}