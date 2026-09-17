package com.danilo.conductorexample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.danilo.conductorexample.ui.addgame.AddGameScreen
import com.danilo.conductorexample.ui.catalog.GamesCatalogScreen
import com.danilo.conductorexample.ui.detail.GameDetailScreen

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = AppDestination.Catalog.route,
        modifier = modifier
    ) {
        composable(AppDestination.Catalog.route) {
            GamesCatalogScreen(
                onAddGameClick = {
                    navController.navigate(AppDestination.AddGame.route)
                },
                onGameClick = { gameId ->
                    navController.navigate(AppDestination.Detail.createRoute(gameId))
                }
            )
        }
        composable(AppDestination.AddGame.route) {
            AddGameScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = AppDestination.Detail.route,
            arguments = listOf(
                navArgument(AppDestination.Detail.ARG_GAME_ID) {
                    type = NavType.StringType
                }
            )
        ) {
            GameDetailScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
