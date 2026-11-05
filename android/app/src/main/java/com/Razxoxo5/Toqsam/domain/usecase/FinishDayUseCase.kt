package com.Razxoxo5.Toqsam.domain.usecase

import com.Razxoxo5.Toqsam.domain.model.Badge
import com.Razxoxo5.Toqsam.domain.model.DayPlan
import com.Razxoxo5.Toqsam.domain.model.DayResult
import com.Razxoxo5.Toqsam.domain.model.LanternScene
import com.Razxoxo5.Toqsam.domain.model.PlayerProgress

class FinishDayUseCase(
    private val fullDayBonus: Int,
    private val badgesPerScene: Int
) {

    data class Outcome(
        val result: DayResult,
        val progress: PlayerProgress
    )

    operator fun invoke(
        plan: DayPlan,
        progress: PlayerProgress,
        badges: List<Badge>,
        scenes: List<LanternScene>
    ): Outcome {
        val completed = plan.completedCount
        val total = plan.totalCount
        val earned = plan.goals.filter { it.completed }.sumOf { it.tokens }
        val bonus = if (total > 0 && completed == total) fullDayBonus else 0
        val tokensEarned = earned + bonus
        val keptStreak = total > 0 && completed * 2 >= total
        val streak = if (keptStreak) progress.streak + 1 else 0
        val fullDays = progress.fullDays + if (total > 0 && completed == total) 1 else 0

        val unlockedBadges = badges
            .filter { it.requiredFullDays <= fullDays }
            .map { it.id }
        val newBadge = unlockedBadges.firstOrNull { it !in progress.badgeIds }
        val badgeIds = (progress.badgeIds + unlockedBadges).distinct()

        val sceneQuota = if (badgesPerScene > 0) badgeIds.size / badgesPerScene else 0
        val unlockedScenes = scenes.take(sceneQuota).map { it.id }
        val newScene = unlockedScenes.firstOrNull { it !in progress.sceneIds }
        val sceneIds = (progress.sceneIds + unlockedScenes).distinct()

        val updated = progress.copy(
            tokens = progress.tokens + tokensEarned,
            streak = streak,
            bestStreak = maxOf(progress.bestStreak, streak),
            totalGoalsCompleted = progress.totalGoalsCompleted + completed,
            fullDays = fullDays,
            badgeIds = badgeIds,
            sceneIds = sceneIds
        )

        val result = DayResult(
            completed = completed,
            total = total,
            tokensEarned = tokensEarned,
            badgeName = badges.firstOrNull { it.id == newBadge }?.name.orEmpty(),
            unlockedScene = scenes.firstOrNull { it.id == newScene }?.name.orEmpty(),
            streak = streak
        )
        return Outcome(result, updated)
    }
}
