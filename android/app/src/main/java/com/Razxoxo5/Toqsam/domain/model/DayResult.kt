package com.Razxoxo5.Toqsam.domain.model

data class DayResult(
    val completed: Int,
    val total: Int,
    val tokensEarned: Int,
    val badgeName: String,
    val unlockedScene: String,
    val streak: Int
)
