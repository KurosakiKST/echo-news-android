package com.ryan.echo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ryan.echo.feature.bookmark.BookmarkScreen
import com.ryan.echo.feature.detail.DetailRoute
import com.ryan.echo.feature.home.HomeRoute
import com.ryan.echo.feature.search.SearchScreen

@Composable
fun EchoNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route,
        modifier = modifier
    ) {
        composable(Routes.Home.route) {
            HomeRoute(
                onArticleClick = { articleId ->
                    navController.navigate(Routes.Detail.createRoute(articleId))
                }
            )
        }

        composable(Routes.Search.route) {
            SearchScreen(
                onArticleClick = { articleId ->
                    navController.navigate(Routes.Detail.createRoute(articleId))
                }
            )
        }

        composable(Routes.Bookmark.route) {
            BookmarkScreen(
                onArticleClick = { articleId ->
                    navController.navigate(Routes.Detail.createRoute(articleId))
                }
            )
        }

        composable(
            route = Routes.Detail.route,
            arguments = listOf(
                navArgument("articleId") {
                    type = NavType.StringType
                }
            )
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getString("articleId") ?: ""
            DetailRoute(
                articleId = articleId,
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    }
}