package com.Razxoxo5.Toqsam.core.navigation

sealed interface Screen {

    data object Splash : Screen

    data object Menu : Screen

    data object Game : Screen

    data object Challenge : Screen

    data object Progress : Screen

    data object Collection : Screen

    data object Settings : Screen

    data class GameOver(
        val completed: Int,
        val total: Int,
        val tokensEarned: Int,
        val badgeName: String,
        val unlockedScene: String,
        val streak: Int
    ) : Screen
}
