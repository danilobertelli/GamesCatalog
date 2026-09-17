package com.danilo.conductorexample.ui.navigation

sealed class AppDestination(val route: String) {
    data object Catalog : AppDestination("catalog")
    data object AddGame : AppDestination("add_game")
}
