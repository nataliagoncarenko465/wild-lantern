package com.Razxoxo5.Toqsam.presentation.menu

data class MenuUiState(
    val tokens: Int = 0,
    val streak: Int = 0,
    val dateLabel: String = "",
    val completedToday: Int = 0,
    val totalToday: Int = 0,
    val unlockedScenes: Int = 0,
    val totalScenes: Int = 0,
    val badges: Int = 0,
    val challengeTitle: String = "",
    val hasPlan: Boolean = false
)
