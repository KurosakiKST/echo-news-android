package com.ryan.echo.feature.bookmark

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ryan.echo.feature.components.ArticleItem
import com.ryan.echo.feature.components.ErrorMessage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    state: BookmarkState,
    onEvent: (BookmarkEvent) -> Unit,
    onArticleClick: (String) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Bookmarks") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.error != null) {
                ErrorMessage(
                    message = state.error,
                    onRetry = { onEvent(BookmarkEvent.LoadBookmarks) },
                    modifier = Modifier.align(Alignment.Center)
                )
            } else if (state.bookmarkedArticles.isEmpty()) {
                Text(
                    text = "No bookmarked articles yet.\n" +
                            "Bookmark articles to save them here for later reading.",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                )
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp)
                ) {
                    items(state.bookmarkedArticles) { article ->
                        ArticleItem(
                            article = article,
                            onArticleClick = { onArticleClick(article.id) },
                            onBookmarkClick = { isBookmarked ->
                                onEvent(
                                    BookmarkEvent.BookmarkArticle(
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