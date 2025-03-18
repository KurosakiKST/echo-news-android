package com.ryan.echo.feature.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeRoute(
    onArticleClick: (String) -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()

    HomeScreen(
        state = state,
        onEvent = viewModel::onEvent,
        onArticleClick = onArticleClick
    )
}