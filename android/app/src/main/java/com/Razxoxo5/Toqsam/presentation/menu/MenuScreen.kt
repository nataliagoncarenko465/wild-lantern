package com.Razxoxo5.Toqsam.presentation.menu

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Razxoxo5.Toqsam.R
import com.Razxoxo5.Toqsam.core.di.ServiceLocator
import com.Razxoxo5.Toqsam.core.theme.BackgroundBase
import com.Razxoxo5.Toqsam.core.theme.LanternGold
import com.Razxoxo5.Toqsam.core.theme.NightBlue
import com.Razxoxo5.Toqsam.core.theme.TextPrimary
import com.Razxoxo5.Toqsam.core.theme.TextSecondary
import com.Razxoxo5.Toqsam.core.theme.TigerTerracotta
import com.Razxoxo5.Toqsam.core.ui.BadgePill
import com.Razxoxo5.Toqsam.core.ui.BentoStartTile
import com.Razxoxo5.Toqsam.core.ui.BentoTile
import com.Razxoxo5.Toqsam.core.ui.GlassCard
import com.Razxoxo5.Toqsam.core.ui.ScreenBackground

@Composable
fun MenuRoute(
    onStartPlanning: () -> Unit,
    onOpenChallenge: () -> Unit,
    onOpenProgress: () -> Unit,
    onOpenCollection: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val menuViewModel: MenuViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by menuViewModel.state.collectAsStateWithLifecycle()

    MenuScreen(
        state = state,
        onStartPlanning = {
            menuViewModel.prepareSession()
            onStartPlanning()
        },
        onOpenChallenge = onOpenChallenge,
        onOpenProgress = onOpenProgress,
        onOpenCollection = onOpenCollection,
        onOpenSettings = onOpenSettings
    )
}

@Composable
private fun MenuScreen(
    state: MenuUiState,
    onStartPlanning: () -> Unit,
    onOpenChallenge: () -> Unit,
    onOpenProgress: () -> Unit,
    onOpenCollection: () -> Unit,
    onOpenSettings: () -> Unit
) {
    val idle = rememberInfiniteTransition(label = "menuIdle")
    val tigerFloat by idle.animateFloat(
        initialValue = -4f,
        targetValue = 4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "tigerFloat"
    )

    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(
                BackgroundBase.copy(alpha = 0.72f),
                BackgroundBase.copy(alpha = 0.86f)
            )
        ),
        accent = Brush.radialGradient(
            colors = listOf(LanternGold.copy(alpha = 0.2f), Color.Transparent),
            radius = 620f
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().height(44.dp)
            ) {
                Image(
                    painter = painterResource(R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = stringResource(R.string.splash_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = TextPrimary
                )
                Spacer(modifier = Modifier.weight(1f))
                BadgePill(
                    iconRes = R.drawable.sprite_5,
                    text = state.tokens.toString(),
                    iconDescription = stringResource(R.string.cd_token)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            GlassCard(
                radius = 22,
                borderColor = LanternGold.copy(alpha = 0.3f),
                modifier = Modifier.fillMaxWidth().height(118.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize().padding(horizontal = 18.dp)
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = stringResource(R.string.menu_greeting),
                            style = MaterialTheme.typography.headlineSmall,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = state.dateLabel,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary,
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(R.drawable.sprite_3),
                                contentDescription = null,
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (state.hasPlan) {
                                    stringResource(R.string.menu_streak, state.streak)
                                } else {
                                    stringResource(R.string.menu_no_goals)
                                },
                                style = MaterialTheme.typography.labelSmall,
                                color = LanternGold,
                                maxLines = 1
                            )
                        }
                    }
                    Image(
                        painter = painterResource(R.drawable.sprite_1),
                        contentDescription = stringResource(R.string.cd_mascot),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .offset(y = tigerFloat.dp)
                            .size(92.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            BentoStartTile(
                title = stringResource(R.string.start_planning),
                caption = stringResource(R.string.todays_goals),
                onClick = onStartPlanning,
                imageRes = R.drawable.sprite_2,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth().height(168.dp)
            ) {
                BentoTile(
                    label = stringResource(R.string.daily_challenge),
                    value = state.challengeTitle,
                    onClick = onOpenChallenge,
                    accent = TigerTerracotta,
                    vectorRes = R.drawable.ic_flame,
                    modifier = Modifier.weight(1f).fillMaxHeight()
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    BentoTile(
                        label = stringResource(R.string.progress),
                        value = if (state.hasPlan) {
                            stringResource(
                                R.string.value_fraction,
                                state.completedToday,
                                state.totalToday
                            )
                        } else {
                            stringResource(R.string.new_day)
                        },
                        onClick = onOpenProgress,
                        accent = NightBlue,
                        vectorRes = R.drawable.ic_trophy,
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                    BentoTile(
                        label = stringResource(R.string.collection),
                        value = stringResource(
                            R.string.value_fraction,
                            state.unlockedScenes,
                            state.totalScenes
                        ),
                        onClick = onOpenCollection,
                        accent = LanternGold,
                        imageRes = R.drawable.sprite_3,
                        imageDescription = stringResource(R.string.cd_badge),
                        modifier = Modifier.fillMaxWidth().weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.height(12.dp))

            BentoTile(
                label = stringResource(R.string.settings),
                value = stringResource(R.string.settings_caption),
                onClick = onOpenSettings,
                accent = TextSecondary,
                vectorRes = R.drawable.ic_settings,
                modifier = Modifier.fillMaxWidth().height(72.dp)
            )
        }
    }
}
