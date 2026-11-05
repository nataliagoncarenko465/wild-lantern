package com.Razxoxo5.Toqsam.presentation.progress

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
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
import com.Razxoxo5.Toqsam.core.theme.BackgroundBase
import com.Razxoxo5.Toqsam.core.theme.LanternGold
import com.Razxoxo5.Toqsam.core.theme.NightBlue
import com.Razxoxo5.Toqsam.core.theme.SurfaceNight
import com.Razxoxo5.Toqsam.core.theme.TextPrimary
import com.Razxoxo5.Toqsam.core.theme.TextSecondary
import com.Razxoxo5.Toqsam.core.theme.TigerTerracotta
import com.Razxoxo5.Toqsam.core.ui.GlassCard
import com.Razxoxo5.Toqsam.core.ui.ScreenBackground
import com.Razxoxo5.Toqsam.core.ui.SecondaryButton
import com.Razxoxo5.Toqsam.core.ui.SectionHeader
import com.Razxoxo5.Toqsam.core.ui.StatCard
import com.Razxoxo5.Toqsam.core.ui.TopBarRow

@Composable
fun ProgressRoute(onMenu: () -> Unit) {
    val progressViewModel: ProgressViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by progressViewModel.state.collectAsStateWithLifecycle()

    ProgressScreen(state = state, onMenu = onMenu)
}

@Composable
private fun ProgressScreen(state: ProgressUiState, onMenu: () -> Unit) {
    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(
                BackgroundBase.copy(alpha = 0.88f),
                NightBlue.copy(alpha = 0.55f),
                BackgroundBase.copy(alpha = 0.94f)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            TopBarRow(title = stringResource(R.string.progress), onBack = onMenu)

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                StatCard(
                    value = state.totalGoals.toString(),
                    label = stringResource(R.string.total_goals),
                    accent = LanternGold,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = state.bestStreak.toString(),
                    label = stringResource(R.string.best_streak),
                    accent = TigerTerracotta,
                    modifier = Modifier.weight(1f)
                )
                StatCard(
                    value = stringResource(
                        R.string.value_fraction,
                        state.badges,
                        state.totalBadges
                    ),
                    label = stringResource(R.string.badges),
                    accent = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            SectionHeader(title = stringResource(R.string.this_week))

            Spacer(modifier = Modifier.height(12.dp))

            WeekStrip(week = state.week)

            Spacer(modifier = Modifier.height(20.dp))

            SectionHeader(title = stringResource(R.string.recent_days))

            Spacer(modifier = Modifier.height(10.dp))

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                if (state.sessions.isEmpty()) {
                    HistoryEmptyState()
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        contentPadding = PaddingValues(bottom = 12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(items = state.sessions, key = { row -> row.id }) { row ->
                            SessionCard(row = row)
                        }
                    }
                }
            }

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
private fun WeekStrip(week: List<DayBar>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Bottom,
        modifier = Modifier.fillMaxWidth().height(80.dp)
    ) {
        week.forEach { day ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    contentAlignment = Alignment.BottomCenter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .background(SurfaceNight.copy(alpha = 0.8f), RoundedCornerShape(10.dp))
                        .then(
                            if (day.isToday) {
                                Modifier.border(
                                    BorderStroke(2.dp, TigerTerracotta),
                                    RoundedCornerShape(10.dp)
                                )
                            } else {
                                Modifier
                            }
                        )
                ) {
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(maxOf(day.ratio, 0.06f))
                            .background(
                                Brush.verticalGradient(listOf(LanternGold, NightBlue)),
                                RoundedCornerShape(10.dp)
                            )
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = day.letter,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (day.isToday) TigerTerracotta else TextSecondary
                )
            }
        }
    }
}

@Composable
private fun SessionCard(row: SessionRow) {
    GlassCard(
        radius = 16,
        borderColor = LanternGold.copy(alpha = 0.25f),
        modifier = Modifier.fillMaxWidth().height(64.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp)
        ) {
            Text(
                text = row.dateLabel,
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary,
                modifier = Modifier.width(80.dp)
            )
            Text(
                text = stringResource(R.string.value_fraction, row.completed, row.total),
                style = MaterialTheme.typography.bodyLarge,
                color = TextSecondary,
                modifier = Modifier.weight(1f)
            )
            Image(
                painter = painterResource(R.drawable.sprite_5),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = stringResource(R.string.value_gain, row.tokens),
                style = MaterialTheme.typography.labelLarge,
                color = LanternGold
            )
        }
    }
}

@Composable
private fun HistoryEmptyState() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.sprite_4),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.size(120.dp).alpha(0.3f)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.no_history),
            style = MaterialTheme.typography.headlineSmall,
            color = TextPrimary,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.finish_first_day),
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            textAlign = TextAlign.Center
        )
    }
}
