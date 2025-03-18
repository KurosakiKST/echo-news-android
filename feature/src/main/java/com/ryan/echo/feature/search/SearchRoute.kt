package com.ryan.echo.feature.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SearchRoute(
    onArticleClick: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    SearchScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onArticleClick = onArticleClick
    )
}