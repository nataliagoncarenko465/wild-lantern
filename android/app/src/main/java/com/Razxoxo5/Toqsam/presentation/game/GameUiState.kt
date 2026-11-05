package com.Razxoxo5.Toqsam.presentation.game

import com.Razxoxo5.Toqsam.domain.model.Goal

data class GameUiState(
    val goals: List<Goal> = emptyList(),
    val tokens: Int = 0,
    val streak: Int = 0,
    val elapsedSeconds: Int = 0,
    val loading: Boolean = true,
    val error: Boolean = false,
    val showAddGoal: Boolean = false,
    val showPause: Boolean = false
) {
    val completed: Int get() = goals.count { it.completed }

    val total: Int get() = goals.size

    val allDone: Boolean get() = total > 0 && completed == total
}
