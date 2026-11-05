package com.Razxoxo5.Toqsam.domain.repository

import com.Razxoxo5.Toqsam.domain.model.Badge
import com.Razxoxo5.Toqsam.domain.model.DailyChallenge
import com.Razxoxo5.Toqsam.domain.model.DayPlan
import com.Razxoxo5.Toqsam.domain.model.LanternScene
import com.Razxoxo5.Toqsam.domain.model.PlayerProgress
import com.Razxoxo5.Toqsam.domain.model.SessionRecord
import kotlinx.coroutines.flow.StateFlow

interface PlannerRepository {

    val plan: StateFlow<DayPlan?>

    val progress: StateFlow<PlayerProgress>

    val history: StateFlow<List<SessionRecord>>

    val loadFailed: StateFlow<Boolean>

    val badgeCatalog: List<Badge>

    val sceneCatalog: List<LanternScene>

    fun today(): String

    fun todayChallenge(): DailyChallenge

    fun ensureTodayPlan(goalsPerDay: Int, includeChallenge: Boolean)

    fun startNewDayPlan(goalsPerDay: Int, includeChallenge: Boolean)

    fun savePlan(plan: DayPlan)

    fun saveProgress(progress: PlayerProgress)

    fun addSession(record: SessionRecord)

    fun resetAll()
}
