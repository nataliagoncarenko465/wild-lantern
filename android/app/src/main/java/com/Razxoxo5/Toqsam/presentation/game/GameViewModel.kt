package com.Razxoxo5.Toqsam.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.Razxoxo5.Toqsam.core.config.GameConfig
import com.Razxoxo5.Toqsam.data.sample.SampleData
import com.Razxoxo5.Toqsam.domain.model.DayResult
import com.Razxoxo5.Toqsam.domain.model.Goal
import com.Razxoxo5.Toqsam.domain.model.SessionRecord
import com.Razxoxo5.Toqsam.domain.repository.PlannerRepository
import com.Razxoxo5.Toqsam.domain.repository.SettingsRepository
import com.Razxoxo5.Toqsam.domain.usecase.FinishDayUseCase
import com.Razxoxo5.Toqsam.domain.usecase.ToggleGoalUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class GameViewModel(
    private val planner: PlannerRepository,
    private val preferences: SettingsRepository,
    private val toggleGoal: ToggleGoalUseCase,
    private val finishDayUseCase: FinishDayUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val finishedEvents = Channel<DayResult>(Channel.BUFFERED)
    val events: Flow<DayResult> = finishedEvents.receiveAsFlow()

    private var tickJob: Job? = null

    init {
        viewModelScope.launch {
            combine(planner.plan, planner.progress, planner.loadFailed) { plan, progress, failed ->
                GameUiState(
                    goals = plan?.goals.orEmpty(),
                    tokens = progress.tokens,
                    streak = progress.streak,
                    loading = plan == null && !failed,
                    error = failed
                )
            }.collect { incoming ->
                _state.update {
                    it.copy(
                        goals = incoming.goals,
                        tokens = incoming.tokens,
                        streak = incoming.streak,
                        loading = incoming.loading,
                        error = incoming.error
                    )
                }
            }
        }
        ensurePlan()
        startTicking()
    }

    fun resume() {
        if (tickJob?.isActive != true) {
            startTicking()
        }
    }

    fun pause() {
        tickJob?.cancel()
        tickJob = null
    }

    fun retry() {
        viewModelScope.launch {
            planner.startNewDayPlan(preferences.settings.value.goalsPerDay, false)
        }
    }

    fun onToggleGoal(goalId: String) {
        val current = planner.plan.value ?: return
        planner.savePlan(toggleGoal(current, goalId))
    }

    fun onAddGoalRequested() {
        _state.update { it.copy(showAddGoal = true) }
    }

    fun onAddGoalDismissed() {
        _state.update { it.copy(showAddGoal = false) }
    }

    fun onAddGoalConfirmed(title: String) {
        val trimmed = title.trim()
        _state.update { it.copy(showAddGoal = false) }
        if (trimmed.isEmpty()) return
        val current = planner.plan.value ?: return
        val goal = Goal(
            id = CUSTOM_PREFIX + current.goals.size + trimmed.hashCode(),
            title = trimmed,
            category = CUSTOM_CATEGORY,
            tokens = GameConfig.TOKENS_PER_GOAL
        )
        planner.savePlan(current.copy(goals = current.goals + goal))
    }

    fun onShuffle() {
        val current = planner.plan.value ?: return
        val replaceable = current.goals.lastOrNull { !it.completed && !it.isChallenge } ?: return
        val taken = current.goals.map { it.id }.toSet()
        val suggestion = SampleData.suggestionFor(taken, current.goals.size + replaceable.id.hashCode())
            ?: return
        val goals = current.goals.map { if (it.id == replaceable.id) suggestion else it }
        planner.savePlan(current.copy(goals = goals))
    }

    fun onPauseRequested() {
        pause()
        _state.update { it.copy(showPause = true) }
    }

    fun onPauseDismissed() {
        _state.update { it.copy(showPause = false) }
        resume()
    }

    fun onFinishDay() {
        val current = planner.plan.value ?: return
        pause()
        val outcome = finishDayUseCase(
            current,
            planner.progress.value,
            planner.badgeCatalog,
            planner.sceneCatalog
        )
        planner.saveProgress(outcome.progress)
        planner.addSession(
            SessionRecord(
                date = planner.today(),
                completed = current.completedCount,
                total = current.totalCount,
                tokens = outcome.result.tokensEarned
            )
        )
        planner.savePlan(current.copy(finished = true))
        _state.update { it.copy(showPause = false, showAddGoal = false, elapsedSeconds = 0) }
        viewModelScope.launch { finishedEvents.send(outcome.result) }
    }

    private fun ensurePlan() {
        viewModelScope.launch {
            planner.ensureTodayPlan(preferences.settings.value.goalsPerDay, false)
        }
    }

    private fun startTicking() {
        tickJob = viewModelScope.launch {
            while (isActive) {
                delay(TICK_MS)
                _state.update { it.copy(elapsedSeconds = it.elapsedSeconds + 1) }
            }
        }
    }

    private companion object {
        const val TICK_MS = 1000L
        const val CUSTOM_PREFIX = "custom_"
        const val CUSTOM_CATEGORY = "CUSTOM"
    }
}
