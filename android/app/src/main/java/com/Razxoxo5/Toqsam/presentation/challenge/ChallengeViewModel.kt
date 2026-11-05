package com.Razxoxo5.Toqsam.presentation.challenge

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Razxoxo5.Toqsam.core.config.GameConfig
import com.Razxoxo5.Toqsam.domain.repository.PlannerRepository
import com.Razxoxo5.Toqsam.domain.repository.SettingsRepository
import com.Razxoxo5.Toqsam.domain.usecase.ClaimChallengeUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChallengeViewModel(
    private val planner: PlannerRepository,
    private val preferences: SettingsRepository,
    private val claimChallenge: ClaimChallengeUseCase
) : ViewModel() {

    val state: StateFlow<ChallengeUiState> = combine(
        planner.plan,
        planner.progress
    ) { plan, progress ->
        val challenge = planner.todayChallenge()
        ChallengeUiState(
            title = challenge.title,
            description = challenge.description,
            tokens = challenge.tokens,
            badgeName = planner.badgeCatalog.firstOrNull { it.id !in progress.badgeIds }
                ?.name
                .orEmpty(),
            steps = minOf(plan?.completedCount ?: 0, GameConfig.CHALLENGE_STEPS),
            totalSteps = GameConfig.CHALLENGE_STEPS,
            claimed = progress.claimedChallengeDate == planner.today()
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
        initialValue = ChallengeUiState()
    )

    fun startChallenge() {
        viewModelScope.launch {
            planner.ensureTodayPlan(preferences.settings.value.goalsPerDay, true)
            planner.saveProgress(claimChallenge(planner.progress.value, planner.today()))
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5000L
    }
}
