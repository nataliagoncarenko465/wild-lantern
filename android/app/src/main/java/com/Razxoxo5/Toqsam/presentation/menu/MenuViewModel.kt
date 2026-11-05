package com.Razxoxo5.Toqsam.presentation.menu

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Razxoxo5.Toqsam.domain.repository.PlannerRepository
import com.Razxoxo5.Toqsam.domain.repository.SettingsRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MenuViewModel(
    private val planner: PlannerRepository,
    private val preferences: SettingsRepository
) : ViewModel() {

    private val dateFormat = DateTimeFormatter.ofPattern("EEEE dd MMMM", Locale.US)

    val state: StateFlow<MenuUiState> = combine(
        planner.plan,
        planner.progress,
        preferences.settings
    ) { plan, progress, settings ->
        MenuUiState(
            tokens = progress.tokens,
            streak = progress.streak,
            dateLabel = LocalDate.now().format(dateFormat).uppercase(Locale.US),
            completedToday = plan?.completedCount ?: 0,
            totalToday = plan?.totalCount ?: settings.goalsPerDay,
            unlockedScenes = progress.sceneIds.size,
            totalScenes = planner.sceneCatalog.size,
            badges = progress.badgeIds.size,
            challengeTitle = planner.todayChallenge().title,
            hasPlan = plan != null && !plan.finished
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
        initialValue = MenuUiState()
    )

    fun prepareSession() {
        viewModelScope.launch {
            planner.ensureTodayPlan(preferences.settings.value.goalsPerDay, false)
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5000L
    }
}
