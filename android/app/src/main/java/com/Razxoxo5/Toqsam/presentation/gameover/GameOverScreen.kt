package com.Razxoxo5.Toqsam.presentation.gameover

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.Razxoxo5.Toqsam.core.di.ServiceLocator
import com.Razxoxo5.Toqsam.core.navigation.Screen
import com.Razxoxo5.Toqsam.core.theme.BackgroundBase
import com.Razxoxo5.Toqsam.core.theme.HighlightEdge
import com.Razxoxo5.Toqsam.core.theme.LanternGold
import com.Razxoxo5.Toqsam.core.theme.LanternGoldDark
import com.Razxoxo5.Toqsam.core.theme.SurfaceElevated
import com.Razxoxo5.Toqsam.core.theme.TextPrimary
import com.Razxoxo5.Toqsam.core.theme.TextSecondary
import com.Razxoxo5.Toqsam.core.theme.TigerTerracotta
import com.Razxoxo5.Toqsam.core.ui.GlassCard
import com.Razxoxo5.Toqsam.core.ui.PrimaryButton
import com.Razxoxo5.Toqsam.core.ui.ScreenBackground
import com.Razxoxo5.Toqsam.core.ui.SecondaryButton
import com.Razxoxo5.Toqsam.core.ui.StatCard

@Composable
fun GameOverRoute(
    result: Screen.GameOver,
    onPlanAgain: () -> Unit,
    onMenu: () -> Unit
) {
    val gameOverViewModel: GameOverViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by gameOverViewModel.state.collectAsStateWithLifecycle()

    GameOverScreen(
        result = result,
        state = state,
        onPlanAgain = {
            gameOverViewModel.planAgain()
            onPlanAgain()
        },
        onMenu = onMenu
    )
}

@Composable
private fun GameOverScreen(
    result: Screen.GameOver,
    state: GameOverUiState,
    onPlanAgain: () -> Unit,
    onMenu: () -> Unit
) {
    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(result) { entered = true }

    val medallionScale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.7f,
        animationSpec = spring(dampingRatio = 0.6f, stiffness = 260f),
        label = "medallionScale"
    )
    val rings = rememberInfiniteTransition(label = "resultRings")
    val outerRing by rings.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "outerRing"
    )
    val innerRing by rings.animateFloat(
        initialValue = 360f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "innerRing"
    )
    val bloom by rings.animateFloat(
        initialValue = 0.45f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "bloom"
    )

    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(
                SurfaceElevated.copy(alpha = 0.8f),
                BackgroundBase.copy(alpha = 0.94f)
            )
        ),
        accent = Brush.radialGradient(
            colors = listOf(TigerTerracotta.copy(alpha = 0.18f), Color.Transparent),
            radius = 700f
        )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(44.dp).alpha(0.5f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .weight(1f)
                        .height(72.dp)
                        .background(
                            Brush.verticalGradient(listOf(LanternGold, LanternGoldDark)),
                            RoundedCornerShape(18.dp)
                        )
                ) {
                    Text(
                        text = stringResource(R.string.day_complete),
                        style = MaterialTheme.typography.headlineMedium,
                        color = BackgroundBase,
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Image(
                    painter = painterResource(R.drawable.sprite_4),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(44.dp).alpha(0.5f)
                )
            }

            Spacer(modifier = Modifier.height(18.dp))

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(168.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(168.dp)
                        .rotate(outerRing)
                        .alpha(bloom)
                        .background(
                            Brush.sweepGradient(
                                listOf(
                                    Color.Transparent,
                                    LanternGold.copy(alpha = 0.45f),
                                    Color.Transparent
                                )
                            ),
                            CircleShape
                        )
                )
                Box(
                    modifier = Modifier
                        .size(134.dp)
                        .rotate(innerRing)
                        .alpha(bloom)
                        .background(
                            Brush.sweepGradient(
                                listOf(
                                    Color.Transparent,
                                    TigerTerracotta.copy(alpha = 0.4f),
                                    Color.Transparent
                                )
                            ),
                            CircleShape
                        )
                )
                Image(
                    painter = painterResource(R.drawable.sprite_3),
                    contentDescription = stringResource(R.string.cd_badge),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(128.dp).scale(medallionScale)
                )
            }

            Text(
                text = if (result.badgeName.isNotEmpty()) {
                    result.badgeName
                } else {
                    stringResource(R.string.badges_collected, state.badges, state.totalBadges)
                },
                style = MaterialTheme.typography.titleMedium,
                color = LanternGold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatCard(
                    value = stringResource(
                        R.string.value_fraction,
                        result.completed,
                        result.total
                    ),
                    label = stringResource(R.string.stat_goals),
                    accent = LanternGold,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = stringResource(R.string.value_gain, result.tokensEarned),
                    label = stringResource(R.string.stat_tokens),
                    accent = TigerTerracotta,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = result.streak.toString(),
                    label = stringResource(R.string.stat_streak),
                    accent = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            if (result.unlockedScene.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))
                GlassCard(
                    borderColor = TigerTerracotta.copy(alpha = 0.6f),
                    modifier = Modifier.fillMaxWidth().height(92.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.sprite_2),
                            contentDescription = null,
                            contentScale = ContentScale.Fit,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.width(14.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.new_scene_unlocked),
                                style = MaterialTheme.typography.labelSmall,
                                color = TigerTerracotta
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = result.unlockedScene,
                                style = MaterialTheme.typography.titleMedium,
                                color = TextPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = stringResource(R.string.keep_streak),
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(12.dp))
            PrimaryButton(
                text = stringResource(R.string.plan_again),
                onClick = onPlanAgain,
                iconRes = R.drawable.ic_star,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                iconRes = R.drawable.ic_back,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Spacer(
                modifier = Modifier
                    .width(64.dp)
                    .height(2.dp)
                    .background(HighlightEdge, RoundedCornerShape(1.dp))
            )
        }
    }
}
