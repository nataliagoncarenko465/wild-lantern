package com.Razxoxo5.Toqsam.domain.model

data class AppSettings(
    val reminderCard: Boolean = true,
    val soundCues: Boolean = true,
    val reducedMotion: Boolean = false,
    val goalsPerDay: Int = 6
)
