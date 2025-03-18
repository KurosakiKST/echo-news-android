package com.ryan.echo.feature.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import com.ryan.echo.feature.components.ArticleItem
import com.ryan.echo.feature.components.ErrorMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onEvent: (HomeEvent) -> Unit,
    onArticleClick: (String) -> Unit
) {
    val pullRefreshState = rememberPullToRefreshState()

    // Handle pull to refresh
    if (pullRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            onEvent(HomeEvent.LoadTopHeadlines)
        }
    }

    // Update pull refresh state based on loading status
    LaunchedEffect(state.isLoading) {
        if (!state.isLoading && pullRefreshState.isRefreshing) {
            pullRefreshState.endRefresh()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Echo News") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .nestedScroll(pullRefreshState.nestedScrollConnection)
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Categories filter
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    item {
                        FilterChip(
                            selected = state.selectedCategory == null,
                            onClick = { onEvent(HomeEvent.SelectCategory(null)) },
                            label = { Text("All") }
                        )
                    }

                    items(state.categories) { category ->
                        FilterChip(
                            modifier = Modifier.padding(start = 8.dp),
                            selected = state.selectedCategory == category,
                            onClick = { onEvent(HomeEvent.SelectCategory(category)) },
                            label = { Text(category.replaceFirstChar { it.uppercase() }) }
                        )
                    }
                }

                Box(modifier = Modifier.fillMaxSize()) {
                    if (state.isLoading && state.articles.isEmpty()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else if (state.error != null && state.articles.isEmpty()) {
                        ErrorMessage(
                            message = state.error,
                            onRetry = { onEvent(HomeEvent.LoadTopHeadlines) },
                            modifier = Modifier.align(Alignment.Center)
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
                                            HomeEvent.BookmarkArticle(
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

            // Pull to refresh container
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullRefreshState
            )
        }
    }
}