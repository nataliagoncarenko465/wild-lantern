package com.Razxoxo5.Toqsam.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Razxoxo5.Toqsam.domain.model.AppSettings
import com.Razxoxo5.Toqsam.domain.repository.PlannerRepository
import com.Razxoxo5.Toqsam.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val planner: PlannerRepository,
    private val preferences: SettingsRepository
) : ViewModel() {

    private val resetRequested = MutableStateFlow(false)

    val state: StateFlow<SettingsUiState> = combine(
        preferences.settings,
        resetRequested
    ) { settings, showReset ->
        SettingsUiState(
            reminderCard = settings.reminderCard,
            soundCues = settings.soundCues,
            reducedMotion = settings.reducedMotion,
            goalsPerDay = settings.goalsPerDay,
            showResetDialog = showReset
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
        initialValue = SettingsUiState()
    )

    fun onReminderChanged(enabled: Boolean) {
        preferences.update(preferences.settings.value.copy(reminderCard = enabled))
    }

    fun onSoundChanged(enabled: Boolean) {
        preferences.update(preferences.settings.value.copy(soundCues = enabled))
    }

    fun onMotionChanged(enabled: Boolean) {
        preferences.update(preferences.settings.value.copy(reducedMotion = enabled))
    }

    fun onGoalsPerDayChanged(count: Int) {
        preferences.update(preferences.settings.value.copy(goalsPerDay = count))
    }

    fun onResetRequested() {
        resetRequested.value = true
    }

    fun onResetDismissed() {
        resetRequested.value = false
    }

    fun onResetConfirmed() {
        viewModelScope.launch {
            planner.resetAll()
            preferences.update(AppSettings())
            resetRequested.value = false
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5000L
    }
}
