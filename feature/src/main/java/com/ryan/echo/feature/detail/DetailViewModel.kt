package com.ryan.echo.feature.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ryan.echo.core.common.util.Resource
import com.ryan.echo.core.domain.usecase.BookmarkArticleUseCase
import com.ryan.echo.core.domain.usecase.GetArticleUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getArticleUseCase: GetArticleUseCase,
    private val bookmarkArticleUseCase: BookmarkArticleUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(DetailState())
    val state: StateFlow<DetailState> = _state.asStateFlow()

    private var articleId: String? = null

    init {
        savedStateHandle.get<String>("articleId")?.let { id ->
            articleId = id
            onEvent(DetailEvent.LoadArticle(id))
        }
    }

    fun onEvent(event: DetailEvent) {
        when (event) {
            is DetailEvent.LoadArticle -> loadArticle(event.articleId)
            is DetailEvent.BookmarkArticle -> bookmarkArticle(event.isBookmarked)
            is DetailEvent.ShareArticle -> shareArticle()
            is DetailEvent.OpenInBrowser -> openInBrowser()
        }
    }

    private fun loadArticle(id: String) {
        articleId = id
        viewModelScope.launch {
            getArticleUseCase(id).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }
                    is Resource.Success -> {
                        _state.update { it.copy(
                            article = result.data,
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

    private fun bookmarkArticle(isBookmarked: Boolean) {
        articleId?.let { id ->
            viewModelScope.launch {
                val result = bookmarkArticleUseCase(id, isBookmarked)
                if (result is Resource.Error) {
                    // Handle error if needed
                }
                // The article flow will be updated automatically from the repository
            }
        }
    }

    private fun shareArticle() {
        // This would be handled by the UI layer that has access to Context
        // We could use a callback or event emitter to communicate this to the UI
    }

    private fun openInBrowser() {
        // This would be handled by the UI layer that has access to Context
        // We could use a callback or event emitter to communicate this to the UI
    }
}