package com.Razxoxo5.Toqsam.domain.model

data class PlayerProgress(
    val tokens: Int = 0,
    val streak: Int = 0,
    val bestStreak: Int = 0,
    val totalGoalsCompleted: Int = 0,
    val fullDays: Int = 0,
    val badgeIds: List<String> = emptyList(),
    val sceneIds: List<String> = emptyList(),
    val claimedChallengeDate: String = ""
)
