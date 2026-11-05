package com.Razxoxo5.Toqsam.presentation.game

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Razxoxo5.Toqsam.R
import com.Razxoxo5.Toqsam.core.config.GameConfig
import com.Razxoxo5.Toqsam.core.di.ServiceLocator
import com.Razxoxo5.Toqsam.core.theme.BackgroundBase
import com.Razxoxo5.Toqsam.core.theme.Hairline
import com.Razxoxo5.Toqsam.core.theme.LanternGold
import com.Razxoxo5.Toqsam.core.theme.NightBlue
import com.Razxoxo5.Toqsam.core.theme.SurfaceNight
import com.Razxoxo5.Toqsam.core.theme.SurfaceSunken
import com.Razxoxo5.Toqsam.core.theme.TextPrimary
import com.Razxoxo5.Toqsam.core.theme.TextSecondary
import com.Razxoxo5.Toqsam.core.theme.TigerTerracotta
import com.Razxoxo5.Toqsam.core.ui.GoalCard
import com.Razxoxo5.Toqsam.core.ui.LanternMeter
import com.Razxoxo5.Toqsam.core.ui.PrimaryButton
import com.Razxoxo5.Toqsam.core.ui.ScreenBackground
import com.Razxoxo5.Toqsam.core.ui.SecondaryButton
import com.Razxoxo5.Toqsam.core.ui.StatCard
import com.Razxoxo5.Toqsam.domain.model.DayResult
import com.Razxoxo5.Toqsam.presentation.dialog.AddGoalDialog
import com.Razxoxo5.Toqsam.presentation.dialog.PauseDialog

@Composable
fun GameRoute(
    onFinished: (DayResult) -> Unit,
    onMenu: () -> Unit
) {
    val gameViewModel: GameViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameViewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        gameViewModel.events.collect { result -> onFinished(result) }
    }

    DisposableEffect(Unit) {
        gameViewModel.resume()
        onDispose { gameViewModel.pause() }
    }

    GameScreen(
        state = state,
        onToggleGoal = gameViewModel::onToggleGoal,
        onAddGoal = gameViewModel::onAddGoalRequested,
        onShuffle = gameViewModel::onShuffle,
        onFinishDay = gameViewModel::onFinishDay,
        onPause = gameViewModel::onPauseRequested,
        onRetry = gameViewModel::retry,
        onMenu = onMenu
    )

    if (state.showAddGoal) {
        AddGoalDialog(
            onConfirm = gameViewModel::onAddGoalConfirmed,
            onDismiss = gameViewModel::onAddGoalDismissed
        )
    }
    if (state.showPause) {
        PauseDialog(
            onResume = gameViewModel::onPauseDismissed,
            onMenu = onMenu
        )
    }
}

@Composable
private fun GameScreen(
    state: GameUiState,
    onToggleGoal: (String) -> Unit,
    onAddGoal: () -> Unit,
    onShuffle: () -> Unit,
    onFinishDay: () -> Unit,
    onPause: () -> Unit,
    onRetry: () -> Unit,
    onMenu: () -> Unit
) {
    val celebration = rememberInfiniteTransition(label = "gameCelebration")
    val ctaPulse by celebration.animateFloat(
        initialValue = 1f,
        targetValue = if (state.allDone) 1.03f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "ctaPulse"
    )

    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(
                BackgroundBase.copy(alpha = 0.9f),
                BackgroundBase.copy(alpha = 0.78f),
                SurfaceSunken.copy(alpha = 0.94f)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
        ) {
            GameHeader(onPause = onPause, onMenu = onMenu)

            Spacer(modifier = Modifier.height(10.dp))

            LanternMeter(
                total = minOf(state.total.coerceAtLeast(1), GameConfig.MAX_LANTERN_SLOTS),
                lit = minOf(state.completed, GameConfig.MAX_LANTERN_SLOTS),
                stateLabel = stringResource(
                    R.string.value_fraction,
                    state.completed,
                    state.total
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 4.dp, bottom = 8.dp)
                        .size(120.dp)
                        .rotate(-8f)
                        .alpha(0.14f)
                )
                when {
                    state.error -> ErrorBoard(onRetry = onRetry)
                    state.loading -> LoadingBoard()
                    state.allDone -> AllDoneBoard()
                    else -> LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            end = 16.dp,
                            top = 4.dp,
                            bottom = 16.dp
                        ),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(items = state.goals, key = { goal -> goal.id }) { goal ->
                            GoalCard(
                                goal = goal,
                                onToggle = { onToggleGoal(goal.id) },
                                stateLabel = if (goal.completed) {
                                    stringResource(R.string.state_completed)
                                } else {
                                    stringResource(R.string.state_pending)
                                }
                            )
                        }
                    }
                }
            }

            GameStatStrip(state = state)

            ControlsPanel(
                dimmed = state.completed == 0,
                ctaScale = ctaPulse,
                onAddGoal = onAddGoal,
                onShuffle = onShuffle,
                onFinishDay = onFinishDay
            )
        }
    }
}

@Composable
private fun GameHeader(onPause: () -> Unit, onMenu: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .background(BackgroundBase.copy(alpha = 0.55f))
            .padding(horizontal = 12.dp)
    ) {
        RoundGlyphButton(
            iconRes = R.drawable.icon_pause,
            description = stringResource(R.string.cd_pause),
            onClick = onPause
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.todays_lanterns),
            style = MaterialTheme.typography.titleMedium,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.weight(1f))
        RoundGlyphButton(
            iconRes = R.drawable.icon_home,
            description = stringResource(R.string.cd_home),
            onClick = onMenu
        )
    }
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(1.dp)
            .background(LanternGold.copy(alpha = 0.25f))
    )
}

@Composable
private fun RoundGlyphButton(iconRes: Int, description: String, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = SurfaceNight.copy(alpha = 0.9f),
        border = BorderStroke(1.dp, LanternGold.copy(alpha = 0.35f)),
        modifier = Modifier.size(48.dp)
    ) {
        IconButton(onClick = onClick, modifier = Modifier.size(48.dp)) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = description,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}

@Composable
private fun LoadingBoard() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.loading),
            style = MaterialTheme.typography.labelSmall,
            color = TextSecondary
        )
    }
}

@Composable
private fun ErrorBoard(onRetry: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(horizontal = 32.dp)
    ) {
        Text(
            text = stringResource(R.string.could_not_load),
            style = MaterialTheme.typography.headlineSmall,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        SecondaryButton(
            text = stringResource(R.string.retry),
            onClick = onRetry,
            borderColor = TigerTerracotta
        )
    }
}

@Composable
private fun AllDoneBoard() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize().padding(horizontal = 28.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_1),
            contentDescription = stringResource(R.string.cd_mascot),
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(110.dp)
        )
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = stringResource(R.string.all_goals_done),
            style = MaterialTheme.typography.headlineSmall,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.finish_to_claim),
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun GameStatStrip(state: GameUiState) {
    val doneValue = stringResource(R.string.value_fraction, state.completed, state.total)
    val doneLabel = stringResource(R.string.stat_done)
    val tokensLabel = stringResource(R.string.stat_tokens)
    val streakLabel = stringResource(R.string.stat_streak)

    val cells = ArrayList<StatCell>(3)
    if (state.total > 0) {
        cells.add(StatCell(doneValue, doneLabel, LanternGold))
    }
    if (state.tokens > 0) {
        cells.add(StatCell(state.tokens.toString(), tokensLabel, TigerTerracotta))
    }
    if (state.streak > 0) {
        cells.add(StatCell(state.streak.toString(), streakLabel, TextPrimary))
    }
    if (cells.size < 2) return

    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        cells.forEach { cell ->
            StatCard(
                value = cell.value,
                label = cell.label,
                accent = cell.accent,
                compact = true,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

private data class StatCell(val value: String, val label: String, val accent: Color)

@Composable
private fun ControlsPanel(
    dimmed: Boolean,
    ctaScale: Float,
    onAddGoal: () -> Unit,
    onShuffle: () -> Unit,
    onFinishDay: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Brush.verticalGradient(listOf(SurfaceNight, SurfaceSunken)),
                RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            )
            .padding(horizontal = 18.dp, vertical = 16.dp)
    ) {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Hairline)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            SecondaryButton(
                text = stringResource(R.string.add_goal),
                onClick = onAddGoal,
                iconRes = R.drawable.ic_plus,
                borderColor = NightBlue,
                modifier = Modifier.weight(1f)
            )
            SecondaryButton(
                text = stringResource(R.string.shuffle),
                onClick = onShuffle,
                iconRes = R.drawable.ic_star,
                borderColor = NightBlue,
                modifier = Modifier.weight(1f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        PrimaryButton(
            text = stringResource(R.string.finish_day),
            onClick = onFinishDay,
            dimmed = dimmed,
            iconRes = R.drawable.ic_check,
            modifier = Modifier.fillMaxWidth().scale(ctaScale)
        )
    }
}
