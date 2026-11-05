package com.Razxoxo5.Toqsam.data.sample

import com.Razxoxo5.Toqsam.core.config.GameConfig
import com.Razxoxo5.Toqsam.domain.model.Badge
import com.Razxoxo5.Toqsam.domain.model.DailyChallenge
import com.Razxoxo5.Toqsam.domain.model.Goal
import com.Razxoxo5.Toqsam.domain.model.LanternScene
import kotlin.math.abs

object SampleData {

    val goalPool: List<Goal> = listOf(
        Goal("morning_lantern", "Light the morning lantern", "SUNRISE", GameConfig.TOKENS_PER_GOAL),
        Goal("feed_the_tiger", "Feed the tiger", "MEALS", GameConfig.TOKENS_PER_GOAL),
        Goal("jungle_walk", "Jungle walk", "MOVEMENT", GameConfig.TOKENS_PER_GOAL),
        Goal("quiet_den", "Quiet den", "CALM", GameConfig.TOKENS_PER_GOAL),
        Goal("sharpen_claws", "Sharpen claws", "FOCUS", GameConfig.TOKENS_PER_GOAL),
        Goal("night_lantern", "Night lantern", "EVENING", GameConfig.TOKENS_PER_GOAL),
        Goal("river_stretch", "River stretch", "MOVEMENT", GameConfig.TOKENS_PER_GOAL),
        Goal("bamboo_tidy", "Tidy the bamboo corner", "HOME", GameConfig.TOKENS_PER_GOAL),
        Goal("warm_tea", "Brew a warm tea", "CALM", GameConfig.TOKENS_PER_GOAL),
        Goal("lantern_sketch", "Sketch a lantern", "CREATIVE", GameConfig.TOKENS_PER_GOAL),
        Goal("call_the_pride", "Call someone you miss", "PEOPLE", GameConfig.TOKENS_PER_GOAL),
        Goal("count_the_stars", "Count the stars", "EVENING", GameConfig.TOKENS_PER_GOAL)
    )

    val challenges: List<DailyChallenge> = listOf(
        DailyChallenge(
            "temple_climb",
            "Climb to the temple gate",
            "Take the long way home and walk one extra mile tonight",
            "CHALLENGE",
            GameConfig.CHALLENGE_TOKENS
        ),
        DailyChallenge(
            "silent_hour",
            "Keep one silent hour",
            "One full hour with no screen and no noise before the lanterns go out",
            "CHALLENGE",
            GameConfig.CHALLENGE_TOKENS
        ),
        DailyChallenge(
            "double_lantern",
            "Light two lanterns before noon",
            "Finish two goals while the morning light is still warm",
            "CHALLENGE",
            GameConfig.CHALLENGE_TOKENS
        ),
        DailyChallenge(
            "monsoon_prep",
            "Prepare for the monsoon",
            "Plan tomorrow in writing before the day ends",
            "CHALLENGE",
            GameConfig.CHALLENGE_TOKENS
        ),
        DailyChallenge(
            "tiger_breath",
            "Breathe like a tiger",
            "Ten slow breaths between two finished goals",
            "CHALLENGE",
            GameConfig.CHALLENGE_TOKENS
        )
    )

    val badges: List<Badge> = listOf(
        Badge("first_spark", "FIRST SPARK", 1),
        Badge("golden_stripe", "GOLDEN STRIPE", 3),
        Badge("lantern_keeper", "LANTERN KEEPER", 7),
        Badge("jungle_sage", "JUNGLE SAGE", 14)
    )

    val scenes: List<LanternScene> = listOf(
        LanternScene("river_lanterns", "RIVER LANTERNS", "Earn your first three badges"),
        LanternScene("bamboo_night", "BAMBOO NIGHT", "Keep a streak of three full days"),
        LanternScene("temple_gate", "TEMPLE GATE", "Finish seven full planning days"),
        LanternScene("tiger_moon", "TIGER MOON", "Collect nine lantern badges"),
        LanternScene("monsoon_glow", "MONSOON GLOW", "Hold a streak through a rainy week"),
        LanternScene("fire_garden", "FIRE GARDEN", "Complete the full badge collection")
    )

    fun challengeForDate(date: String): DailyChallenge =
        challenges[abs(date.hashCode()) % challenges.size]

    fun seedGoals(date: String, count: Int): List<Goal> {
        val start = abs(date.hashCode()) % goalPool.size
        return (0 until count.coerceIn(1, goalPool.size)).map { index ->
            goalPool[(start + index) % goalPool.size]
        }
    }

    fun challengeGoal(challenge: DailyChallenge): Goal = Goal(
        id = "challenge_" + challenge.id,
        title = challenge.title,
        category = challenge.category,
        tokens = challenge.tokens,
        isChallenge = true
    )

    fun suggestionFor(taken: Set<String>, seed: Int): Goal? =
        goalPool.filter { it.id !in taken }
            .let { free -> if (free.isEmpty()) null else free[abs(seed) % free.size] }
}
