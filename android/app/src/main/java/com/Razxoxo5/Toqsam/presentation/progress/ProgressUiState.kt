package com.Razxoxo5.Toqsam.presentation.progress

data class DayBar(
    val letter: String,
    val ratio: Float,
    val isToday: Boolean
)

data class SessionRow(
    val id: String,
    val dateLabel: String,
    val completed: Int,
    val total: Int,
    val tokens: Int
)

data class ProgressUiState(
    val totalGoals: Int = 0,
    val bestStreak: Int = 0,
    val badges: Int = 0,
    val totalBadges: Int = 0,
    val week: List<DayBar> = emptyList(),
    val sessions: List<SessionRow> = emptyList()
)
