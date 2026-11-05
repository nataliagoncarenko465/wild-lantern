package com.Razxoxo5.Toqsam.presentation.settings

import com.Razxoxo5.Toqsam.core.config.GameConfig

data class SettingsUiState(
    val reminderCard: Boolean = true,
    val soundCues: Boolean = true,
    val reducedMotion: Boolean = false,
    val goalsPerDay: Int = GameConfig.DEFAULT_GOALS_PER_DAY,
    val showResetDialog: Boolean = false
)
