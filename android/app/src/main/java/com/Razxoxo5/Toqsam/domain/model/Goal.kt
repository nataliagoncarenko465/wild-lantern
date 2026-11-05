package com.Razxoxo5.Toqsam.domain.model

data class Goal(
    val id: String,
    val title: String,
    val category: String,
    val tokens: Int,
    val isChallenge: Boolean = false,
    val completed: Boolean = false
)
