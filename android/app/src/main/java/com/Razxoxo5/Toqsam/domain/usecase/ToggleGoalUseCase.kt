package com.Razxoxo5.Toqsam.domain.usecase

import com.Razxoxo5.Toqsam.domain.model.DayPlan

class ToggleGoalUseCase {

    operator fun invoke(plan: DayPlan, goalId: String): DayPlan {
        val goals = plan.goals.map { goal ->
            if (goal.id == goalId) goal.copy(completed = !goal.completed) else goal
        }
        return plan.copy(goals = goals)
    }
}
