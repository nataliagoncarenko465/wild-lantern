package com.Razxoxo5.Toqsam.presentation.app

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Razxoxo5.Toqsam.core.di.ServiceLocator
import com.Razxoxo5.Toqsam.core.navigation.Screen
import com.Razxoxo5.Toqsam.presentation.challenge.ChallengeRoute
import com.Razxoxo5.Toqsam.presentation.collection.CollectionRoute
import com.Razxoxo5.Toqsam.presentation.game.GameRoute
import com.Razxoxo5.Toqsam.presentation.gameover.GameOverRoute
import com.Razxoxo5.Toqsam.presentation.menu.MenuRoute
import com.Razxoxo5.Toqsam.presentation.progress.ProgressRoute
import com.Razxoxo5.Toqsam.presentation.settings.SettingsRoute
import com.Razxoxo5.Toqsam.presentation.splash.SplashRoute

@Composable
fun AppRoot() {
    val appViewModel: AppViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val screen by appViewModel.screen.collectAsStateWithLifecycle()
    val canGoBack by appViewModel.canGoBack.collectAsStateWithLifecycle()

    BackHandler(enabled = canGoBack) { appViewModel.back() }

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        AnimatedContent(
            targetState = screen,
            transitionSpec = {
                fadeIn(animationSpec = tween(durationMillis = 420)) togetherWith
                    fadeOut(animationSpec = tween(durationMillis = 320))
            },
            label = "screenHost"
        ) { target ->
            when (target) {
                Screen.Splash -> SplashRoute(
                    onFinished = { appViewModel.navigate(Screen.Menu) }
                )

                Screen.Menu -> MenuRoute(
                    onStartPlanning = { appViewModel.navigate(Screen.Game) },
                    onOpenChallenge = { appViewModel.navigate(Screen.Challenge) },
                    onOpenProgress = { appViewModel.navigate(Screen.Progress) },
                    onOpenCollection = { appViewModel.navigate(Screen.Collection) },
                    onOpenSettings = { appViewModel.navigate(Screen.Settings) }
                )

                Screen.Game -> GameRoute(
                    onFinished = { result ->
                        appViewModel.navigate(
                            Screen.GameOver(
                                completed = result.completed,
                                total = result.total,
                                tokensEarned = result.tokensEarned,
                                badgeName = result.badgeName,
                                unlockedScene = result.unlockedScene,
                                streak = result.streak
                            )
                        )
                    },
                    onMenu = { appViewModel.backToMenu() }
                )

                is Screen.GameOver -> GameOverRoute(
                    result = target,
                    onPlanAgain = { appViewModel.restart(Screen.Game) },
                    onMenu = { appViewModel.backToMenu() }
                )

                Screen.Challenge -> ChallengeRoute(
                    onStartChallenge = { appViewModel.navigate(Screen.Game) },
                    onMenu = { appViewModel.backToMenu() }
                )

                Screen.Progress -> ProgressRoute(
                    onMenu = { appViewModel.backToMenu() }
                )

                Screen.Collection -> CollectionRoute(
                    onMenu = { appViewModel.backToMenu() }
                )

                Screen.Settings -> SettingsRoute(
                    onMenu = { appViewModel.backToMenu() }
                )
            }
        }
    }
}
