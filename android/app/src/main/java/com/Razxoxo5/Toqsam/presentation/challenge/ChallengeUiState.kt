package com.Razxoxo5.Toqsam.presentation.challenge

data class ChallengeUiState(
    val title: String = "",
    val description: String = "",
    val tokens: Int = 0,
    val badgeName: String = "",
    val steps: Int = 0,
    val totalSteps: Int = 3,
    val claimed: Boolean = false
)
