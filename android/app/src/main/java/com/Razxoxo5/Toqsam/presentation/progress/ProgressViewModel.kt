package com.Razxoxo5.Toqsam.presentation.progress

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Razxoxo5.Toqsam.domain.model.SessionRecord
import com.Razxoxo5.Toqsam.domain.repository.PlannerRepository
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class ProgressViewModel(
    private val planner: PlannerRepository
) : ViewModel() {

    private val rowFormat = DateTimeFormatter.ofPattern("dd MMM", Locale.US)

    val state: StateFlow<ProgressUiState> = combine(
        planner.progress,
        planner.history
    ) { progress, history ->
        ProgressUiState(
            totalGoals = progress.totalGoalsCompleted,
            bestStreak = progress.bestStreak,
            badges = progress.badgeIds.size,
            totalBadges = planner.badgeCatalog.size,
            week = buildWeek(history),
            sessions = history.take(MAX_ROWS).map { record -> toRow(record) }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
        initialValue = ProgressUiState()
    )

    private fun buildWeek(history: List<SessionRecord>): List<DayBar> {
        val today = LocalDate.now()
        return (6 downTo 0).map { back ->
            val day = today.minusDays(back.toLong())
            val key = day.toString()
            val record = history.firstOrNull { it.date == key }
            val ratio = if (record != null && record.total > 0) {
                record.completed.toFloat() / record.total.toFloat()
            } else {
                0f
            }
            DayBar(
                letter = day.dayOfWeek
                    .getDisplayName(TextStyle.NARROW, Locale.US)
                    .uppercase(Locale.US),
                ratio = ratio.coerceIn(0f, 1f),
                isToday = back == 0
            )
        }
    }

    private fun toRow(record: SessionRecord): SessionRow {
        val label = runCatching {
            LocalDate.parse(record.date).format(rowFormat).uppercase(Locale.US)
        }.getOrDefault(record.date)
        return SessionRow(
            id = record.date + record.tokens + record.completed,
            dateLabel = label,
            completed = record.completed,
            total = record.total,
            tokens = record.tokens
        )
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5000L
        const val MAX_ROWS = 12
    }
}
