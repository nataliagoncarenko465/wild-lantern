package com.Razxoxo5.Toqsam.domain.model

data class DayPlan(
    val date: String,
    val goals: List<Goal>,
    val finished: Boolean = false
) {
    val completedCount: Int get() = goals.count { it.completed }
    val totalCount: Int get() = goals.size
    val allDone: Boolean get() = goals.isNotEmpty() && completedCount == totalCount
}
