package com.Razxoxo5.Toqsam.data.repository

import com.Razxoxo5.Toqsam.data.local.WildLanternPreferences
import com.Razxoxo5.Toqsam.data.sample.SampleData
import com.Razxoxo5.Toqsam.domain.model.Badge
import com.Razxoxo5.Toqsam.domain.model.DailyChallenge
import com.Razxoxo5.Toqsam.domain.model.DayPlan
import com.Razxoxo5.Toqsam.domain.model.Goal
import com.Razxoxo5.Toqsam.domain.model.LanternScene
import com.Razxoxo5.Toqsam.domain.model.PlayerProgress
import com.Razxoxo5.Toqsam.domain.model.SessionRecord
import com.Razxoxo5.Toqsam.domain.repository.PlannerRepository
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class PlannerRepositoryImpl(
    private val store: WildLanternPreferences
) : PlannerRepository {

    private val _plan = MutableStateFlow<DayPlan?>(null)
    override val plan: StateFlow<DayPlan?> = _plan.asStateFlow()

    private val _progress = MutableStateFlow(readProgress())
    override val progress: StateFlow<PlayerProgress> = _progress.asStateFlow()

    private val _history = MutableStateFlow(readHistory())
    override val history: StateFlow<List<SessionRecord>> = _history.asStateFlow()

    private val _loadFailed = MutableStateFlow(false)
    override val loadFailed: StateFlow<Boolean> = _loadFailed.asStateFlow()

    override val badgeCatalog: List<Badge> = SampleData.badges

    override val sceneCatalog: List<LanternScene> = SampleData.scenes

    override fun today(): String = LocalDate.now().toString()

    override fun todayChallenge(): DailyChallenge = SampleData.challengeForDate(today())

    override fun ensureTodayPlan(goalsPerDay: Int, includeChallenge: Boolean) {
        val outcome = runCatching { readPlan() }
        if (outcome.isFailure) {
            _loadFailed.value = true
            _plan.value = null
            return
        }
        _loadFailed.value = false
        val usable = outcome.getOrNull()
            ?.takeIf { it.date == today() && !it.finished && it.goals.isNotEmpty() }
        if (usable == null) {
            startNewDayPlan(goalsPerDay, includeChallenge)
            return
        }
        val withChallenge = if (includeChallenge && usable.goals.none { it.isChallenge }) {
            usable.copy(goals = usable.goals + SampleData.challengeGoal(todayChallenge()))
        } else {
            usable
        }
        _plan.value = withChallenge
        persist(withChallenge)
    }

    override fun startNewDayPlan(goalsPerDay: Int, includeChallenge: Boolean) {
        _loadFailed.value = false
        val base = SampleData.seedGoals(today() + goalsPerDay, goalsPerDay)
        val goals = if (includeChallenge) base + SampleData.challengeGoal(todayChallenge()) else base
        val fresh = DayPlan(date = today(), goals = goals)
        _plan.value = fresh
        persist(fresh)
    }

    override fun savePlan(plan: DayPlan) {
        _plan.value = plan
        persist(plan)
    }

    override fun saveProgress(progress: PlayerProgress) {
        _progress.value = progress
        store.writeInt(KEY_TOKENS, progress.tokens)
        store.writeInt(KEY_STREAK, progress.streak)
        store.writeInt(KEY_BEST_STREAK, progress.bestStreak)
        store.writeInt(KEY_TOTAL_GOALS, progress.totalGoalsCompleted)
        store.writeInt(KEY_FULL_DAYS, progress.fullDays)
        store.writeString(KEY_BADGES, progress.badgeIds.joinToString(LIST_SEPARATOR))
        store.writeString(KEY_SCENES, progress.sceneIds.joinToString(LIST_SEPARATOR))
        store.writeString(KEY_CLAIMED, progress.claimedChallengeDate)
    }

    override fun addSession(record: SessionRecord) {
        val updated = (listOf(record) + _history.value).take(MAX_HISTORY)
        _history.value = updated
        val encoded = updated.joinToString(RECORD_SEPARATOR) { item ->
            listOf(item.date, item.completed, item.total, item.tokens)
                .joinToString(FIELD_SEPARATOR)
        }
        store.writeString(KEY_HISTORY, encoded)
    }

    override fun resetAll() {
        store.clearAll()
        _plan.value = null
        _progress.value = PlayerProgress()
        _history.value = emptyList()
        _loadFailed.value = false
    }

    private fun persist(plan: DayPlan) {
        val head = listOf(plan.date, plan.finished.toString()).joinToString(FIELD_SEPARATOR)
        val body = plan.goals.joinToString(RECORD_SEPARATOR) { goal ->
            listOf(
                goal.id,
                sanitize(goal.title),
                sanitize(goal.category),
                goal.tokens,
                goal.isChallenge,
                goal.completed
            ).joinToString(FIELD_SEPARATOR)
        }
        store.writeString(KEY_PLAN, head + RECORD_SEPARATOR + body)
    }

    private fun readPlan(): DayPlan? {
        val raw = store.readString(KEY_PLAN, "")
        if (raw.isBlank()) return null
        val rows = raw.split(RECORD_SEPARATOR)
        val head = rows.first().split(FIELD_SEPARATOR)
        if (head.size < 2) throw IllegalStateException(KEY_PLAN)
        val goals = rows.drop(1).filter { it.isNotBlank() }.map { row ->
            val parts = row.split(FIELD_SEPARATOR)
            if (parts.size < 6) throw IllegalStateException(KEY_PLAN)
            Goal(
                id = parts[0],
                title = parts[1],
                category = parts[2],
                tokens = parts[3].toInt(),
                isChallenge = parts[4].toBooleanStrict(),
                completed = parts[5].toBooleanStrict()
            )
        }
        return DayPlan(date = head[0], goals = goals, finished = head[1].toBooleanStrict())
    }

    private fun readProgress(): PlayerProgress = PlayerProgress(
        tokens = store.readInt(KEY_TOKENS, 0),
        streak = store.readInt(KEY_STREAK, 0),
        bestStreak = store.readInt(KEY_BEST_STREAK, 0),
        totalGoalsCompleted = store.readInt(KEY_TOTAL_GOALS, 0),
        fullDays = store.readInt(KEY_FULL_DAYS, 0),
        badgeIds = splitList(store.readString(KEY_BADGES, "")),
        sceneIds = splitList(store.readString(KEY_SCENES, "")),
        claimedChallengeDate = store.readString(KEY_CLAIMED, "")
    )

    private fun readHistory(): List<SessionRecord> {
        val raw = store.readString(KEY_HISTORY, "")
        if (raw.isBlank()) return emptyList()
        return raw.split(RECORD_SEPARATOR).mapNotNull { row ->
            val parts = row.split(FIELD_SEPARATOR)
            if (parts.size < 4) return@mapNotNull null
            val completed = parts[1].toIntOrNull() ?: return@mapNotNull null
            val total = parts[2].toIntOrNull() ?: return@mapNotNull null
            val tokens = parts[3].toIntOrNull() ?: return@mapNotNull null
            SessionRecord(parts[0], completed, total, tokens)
        }
    }

    private fun splitList(raw: String): List<String> =
        if (raw.isBlank()) emptyList() else raw.split(LIST_SEPARATOR).filter { it.isNotBlank() }

    private fun sanitize(value: String): String =
        value.replace(FIELD_SEPARATOR, " ").replace(RECORD_SEPARATOR, " ").trim()

    private companion object {
        const val FIELD_SEPARATOR = "|"
        const val RECORD_SEPARATOR = "\n"
        const val LIST_SEPARATOR = ","
        const val MAX_HISTORY = 30
        const val KEY_PLAN = "plan"
        const val KEY_HISTORY = "history"
        const val KEY_TOKENS = "tokens"
        const val KEY_STREAK = "streak"
        const val KEY_BEST_STREAK = "best_streak"
        const val KEY_TOTAL_GOALS = "total_goals"
        const val KEY_FULL_DAYS = "full_days"
        const val KEY_BADGES = "badges"
        const val KEY_SCENES = "scenes"
        const val KEY_CLAIMED = "claimed_challenge"
    }
}
