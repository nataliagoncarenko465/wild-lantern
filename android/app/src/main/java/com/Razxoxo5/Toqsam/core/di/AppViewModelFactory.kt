package com.Razxoxo5.Toqsam.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.Razxoxo5.Toqsam.core.config.GameConfig
import com.Razxoxo5.Toqsam.domain.repository.PlannerRepository
import com.Razxoxo5.Toqsam.domain.repository.SettingsRepository
import com.Razxoxo5.Toqsam.domain.usecase.ClaimChallengeUseCase
import com.Razxoxo5.Toqsam.domain.usecase.FinishDayUseCase
import com.Razxoxo5.Toqsam.domain.usecase.ToggleGoalUseCase
import com.Razxoxo5.Toqsam.presentation.app.AppViewModel
import com.Razxoxo5.Toqsam.presentation.challenge.ChallengeViewModel
import com.Razxoxo5.Toqsam.presentation.collection.CollectionViewModel
import com.Razxoxo5.Toqsam.presentation.game.GameViewModel
import com.Razxoxo5.Toqsam.presentation.gameover.GameOverViewModel
import com.Razxoxo5.Toqsam.presentation.menu.MenuViewModel
import com.Razxoxo5.Toqsam.presentation.progress.ProgressViewModel
import com.Razxoxo5.Toqsam.presentation.settings.SettingsViewModel
import com.Razxoxo5.Toqsam.presentation.splash.SplashViewModel

class AppViewModelFactory(
    private val planner: PlannerRepository,
    private val preferences: SettingsRepository
) : ViewModelProvider.Factory {

    private val toggleGoal = ToggleGoalUseCase()
    private val finishDay = FinishDayUseCase(GameConfig.FULL_DAY_BONUS, GameConfig.BADGES_PER_SCENE)
    private val claimChallenge = ClaimChallengeUseCase()

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) ->
                AppViewModel()

            modelClass.isAssignableFrom(SplashViewModel::class.java) ->
                SplashViewModel()

            modelClass.isAssignableFrom(MenuViewModel::class.java) ->
                MenuViewModel(planner, preferences)

            modelClass.isAssignableFrom(GameViewModel::class.java) ->
                GameViewModel(planner, preferences, toggleGoal, finishDay)

            modelClass.isAssignableFrom(GameOverViewModel::class.java) ->
                GameOverViewModel(planner, preferences)

            modelClass.isAssignableFrom(ChallengeViewModel::class.java) ->
                ChallengeViewModel(planner, preferences, claimChallenge)

            modelClass.isAssignableFrom(ProgressViewModel::class.java) ->
                ProgressViewModel(planner)

            modelClass.isAssignableFrom(CollectionViewModel::class.java) ->
                CollectionViewModel(planner)

            modelClass.isAssignableFrom(SettingsViewModel::class.java) ->
                SettingsViewModel(planner, preferences)

            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
