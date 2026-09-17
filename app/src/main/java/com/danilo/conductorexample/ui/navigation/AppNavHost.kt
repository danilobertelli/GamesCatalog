package com.danilo.conductorexample.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.danilo.conductorexample.ui.catalog.GamesCatalogScreen

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
                }
            )
        }
        composable(AppDestination.AddGame.route) {
            // Will host AddGameScreen in Phase 4
        }
    }
}
