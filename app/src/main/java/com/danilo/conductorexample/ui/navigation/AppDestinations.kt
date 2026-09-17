package com.danilo.conductorexample.ui.navigation

sealed class AppDestination(val route: String) {
    data object Catalog : AppDestination("catalog")
    data object AddGame : AppDestination("add_game")
    data object Detail : AppDestination("game_detail/{gameId}") {
        const val ARG_GAME_ID = "gameId"
        fun createRoute(gameId: String): String = "game_detail/$gameId"
    }
}
