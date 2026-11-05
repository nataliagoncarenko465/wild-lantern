package com.Razxoxo5.Toqsam.presentation.challenge

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
import androidx.compose.material3.Icon
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
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Razxoxo5.Toqsam.R
import com.Razxoxo5.Toqsam.core.di.ServiceLocator
import com.Razxoxo5.Toqsam.core.theme.BackgroundBase
import com.Razxoxo5.Toqsam.core.theme.LanternGold
import com.Razxoxo5.Toqsam.core.theme.NightBlue
import com.Razxoxo5.Toqsam.core.theme.SurfaceNight
import com.Razxoxo5.Toqsam.core.theme.TextPrimary
import com.Razxoxo5.Toqsam.core.theme.TextSecondary
import com.Razxoxo5.Toqsam.core.theme.TigerTerracotta
import com.Razxoxo5.Toqsam.core.ui.GlassCard
import com.Razxoxo5.Toqsam.core.ui.PrimaryButton
import com.Razxoxo5.Toqsam.core.ui.ScreenBackground
import com.Razxoxo5.Toqsam.core.ui.SecondaryButton
import com.Razxoxo5.Toqsam.core.ui.SectionHeader
import com.Razxoxo5.Toqsam.core.ui.TopBarRow

@Composable
fun ChallengeRoute(
    onStartChallenge: () -> Unit,
    onMenu: () -> Unit
) {
    val challengeViewModel: ChallengeViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by challengeViewModel.state.collectAsStateWithLifecycle()

    ChallengeScreen(
        state = state,
        onStartChallenge = {
            challengeViewModel.startChallenge()
            onStartChallenge()
        },
        onMenu = onMenu
    )
}

@Composable
private fun ChallengeScreen(
    state: ChallengeUiState,
    onStartChallenge: () -> Unit,
    onMenu: () -> Unit
) {
    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(
                BackgroundBase.copy(alpha = 0.86f),
                SurfaceNight.copy(alpha = 0.92f)
            )
        ),
        accent = Brush.radialGradient(
            colors = listOf(TigerTerracotta.copy(alpha = 0.22f), Color.Transparent),
            radius = 560f
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            TopBarRow(title = stringResource(R.string.daily_challenge), onBack = onMenu)

            Spacer(modifier = Modifier.height(16.dp))

            GlassCard(
                radius = 24,
                borderColor = TigerTerracotta.copy(alpha = 0.55f),
                modifier = Modifier.fillMaxWidth().height(180.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxSize().padding(16.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.sprite_1),
                        contentDescription = stringResource(R.string.cd_mascot),
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(84.dp)
                    )
                    Spacer(modifier = Modifier.width(14.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = state.title,
                            style = MaterialTheme.typography.headlineSmall,
                            color = TextPrimary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            SectionHeader(title = stringResource(R.string.challenge_rail))

            Spacer(modifier = Modifier.height(12.dp))

            ChallengeRail(steps = state.steps, totalSteps = state.totalSteps)

            Spacer(modifier = Modifier.height(20.dp))

            SectionHeader(title = stringResource(R.string.challenge_reward))

            Spacer(modifier = Modifier.height(12.dp))

            val collectionLabel = stringResource(R.string.collection)
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                RewardChip(
                    imageRes = R.drawable.sprite_5,
                    description = stringResource(R.string.cd_token),
                    text = stringResource(R.string.value_gain, state.tokens)
                )
                RewardChip(
                    imageRes = R.drawable.sprite_3,
                    description = stringResource(R.string.cd_badge),
                    text = if (state.badgeName.isEmpty()) collectionLabel else state.badgeName
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            if (state.claimed) {
                SecondaryButton(
                    text = stringResource(R.string.claimed_today),
                    onClick = onMenu,
                    enabled = false,
                    iconRes = R.drawable.ic_lock,
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                PrimaryButton(
                    text = stringResource(R.string.start_challenge),
                    onClick = onStartChallenge,
                    iconRes = R.drawable.ic_flame,
                    height = 56,
                    modifier = Modifier.fillMaxWidth()
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                iconRes = R.drawable.ic_back,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ChallengeRail(steps: Int, totalSteps: Int) {
    val stepLabel = stringResource(R.string.challenge_step, steps, totalSteps)
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .semantics { stateDescription = stepLabel }
    ) {
        repeat(totalSteps) { index ->
            val reached = index < steps
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .background(
                        if (reached) TigerTerracotta.copy(alpha = 0.22f) else SurfaceNight,
                        RoundedCornerShape(16.dp)
                    )
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            if (reached) TigerTerracotta else NightBlue,
                            CircleShape
                        )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_flame),
                        contentDescription = null,
                        tint = if (reached) BackgroundBase else TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun RewardChip(imageRes: Int, description: String, text: String) {
    GlassCard(
        radius = 16,
        borderColor = LanternGold.copy(alpha = 0.4f),
        modifier = Modifier.height(56.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 14.dp).fillMaxSize()
        ) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = description,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary,
                maxLines = 1
            )
        }
    }
}
