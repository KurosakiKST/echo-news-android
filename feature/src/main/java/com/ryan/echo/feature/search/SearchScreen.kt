package com.ryan.echo.feature.search

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.ryan.echo.feature.components.ArticleItem
import com.ryan.echo.feature.components.ErrorMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    state: SearchState,
    onEvent: (SearchEvent) -> Unit,
    onArticleClick: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            SearchBar(
                query = state.query,
                onQueryChange = {
                    onEvent(SearchEvent.UpdateQuery(it))
                },
                onSearch = {
                    onEvent(SearchEvent.PerformSearch)
                    focusManager.clearFocus()
                },
                active = state.isSearching,
                onActiveChange = { isActive ->
                    if (!isActive) {
                        focusManager.clearFocus()
                    }
                },
                placeholder = { Text("Search news...") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search"
                    )
                },
                trailingIcon = {
                    if (state.query.isNotEmpty()) {
                        IconButton(
                            onClick = { onEvent(SearchEvent.ClearSearch) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Clear"
                            )
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Search suggestions can go here
            }

            Box(modifier = Modifier.fillMaxSize()) {
                if (state.query.isEmpty()) {
                    // Initial state - no search performed yet
                    Text(
                        text = "Search for news by typing in the search bar above",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                } else if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (state.error != null) {
                    ErrorMessage(
                        message = state.error,
                        onRetry = { onEvent(SearchEvent.PerformSearch) },
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else if (state.articles.isEmpty()) {
                    Text(
                        text = "No results found for '${state.query}'",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp)
                    )
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(state.articles) { article ->
                            ArticleItem(
                                article = article,
                                onArticleClick = { onArticleClick(article.id) },
                                onBookmarkClick = { isBookmarked ->
                                    onEvent(
                                        SearchEvent.BookmarkArticle(
                                            articleId = article.id,
                                            isBookmarked = isBookmarked
                                        )
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}