package com.ryan.echo.feature.bookmark

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun BookmarkRoute(
    onArticleClick: (String) -> Unit,
    viewModel: BookmarkViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    // Load bookmarks when the screen is first displayed
    LaunchedEffect(Unit) {
        viewModel.onEvent(BookmarkEvent.LoadBookmarks)
    }

    BookmarkScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onArticleClick = onArticleClick
    )
}