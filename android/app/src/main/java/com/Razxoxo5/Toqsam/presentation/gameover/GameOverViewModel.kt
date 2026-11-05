package com.Razxoxo5.Toqsam.presentation.gameover

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Razxoxo5.Toqsam.domain.repository.PlannerRepository
import com.Razxoxo5.Toqsam.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class GameOverUiState(
    val tokens: Int = 0,
    val bestStreak: Int = 0,
    val badges: Int = 0,
    val totalBadges: Int = 0
)

class GameOverViewModel(
    private val planner: PlannerRepository,
    private val preferences: SettingsRepository
) : ViewModel() {

    val state: StateFlow<GameOverUiState> = planner.progress
        .map { progress ->
            GameOverUiState(
                tokens = progress.tokens,
                bestStreak = progress.bestStreak,
                badges = progress.badgeIds.size,
                totalBadges = planner.badgeCatalog.size
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = GameOverUiState()
        )

    fun planAgain() {
        viewModelScope.launch {
            planner.startNewDayPlan(preferences.settings.value.goalsPerDay, false)
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5000L
    }
}
