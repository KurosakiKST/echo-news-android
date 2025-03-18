package com.ryan.echo.navigation

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Search : Routes("search")
    object Bookmark : Routes("bookmark")
    object Detail : Routes("detail/{articleId}") {
        fun createRoute(articleId: String) = "detail/$articleId"
    }
}