package com.Razxoxo5.Toqsam.presentation.settings

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.Razxoxo5.Toqsam.R
import com.Razxoxo5.Toqsam.core.config.GameConfig
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
import com.Razxoxo5.Toqsam.core.ui.TopBarRow
import com.Razxoxo5.Toqsam.presentation.dialog.ConfirmResetDialog

@Composable
fun SettingsRoute(onMenu: () -> Unit) {
    val settingsViewModel: SettingsViewModel = viewModel(factory = ServiceLocator.viewModelFactory)
    val state by settingsViewModel.state.collectAsStateWithLifecycle()

    SettingsScreen(
        state = state,
        onReminderChanged = settingsViewModel::onReminderChanged,
        onSoundChanged = settingsViewModel::onSoundChanged,
        onMotionChanged = settingsViewModel::onMotionChanged,
        onGoalsPerDayChanged = settingsViewModel::onGoalsPerDayChanged,
        onResetRequested = settingsViewModel::onResetRequested,
        onMenu = onMenu
    )

    if (state.showResetDialog) {
        ConfirmResetDialog(
            onConfirm = settingsViewModel::onResetConfirmed,
            onDismiss = settingsViewModel::onResetDismissed
        )
    }
}

@Composable
private fun SettingsScreen(
    state: SettingsUiState,
    onReminderChanged: (Boolean) -> Unit,
    onSoundChanged: (Boolean) -> Unit,
    onMotionChanged: (Boolean) -> Unit,
    onGoalsPerDayChanged: (Int) -> Unit,
    onResetRequested: () -> Unit,
    onMenu: () -> Unit
) {
    val onLabel = stringResource(R.string.state_on)
    val offLabel = stringResource(R.string.state_off)

    ScreenBackground(
        overlay = Brush.verticalGradient(
            listOf(
                BackgroundBase.copy(alpha = 0.9f),
                SurfaceNight.copy(alpha = 0.95f)
            )
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            TopBarRow(title = stringResource(R.string.settings), onBack = onMenu)

            Spacer(modifier = Modifier.height(16.dp))

            SwitchRow(
                label = stringResource(R.string.setting_reminder),
                checked = state.reminderCard,
                stateLabel = if (state.reminderCard) onLabel else offLabel,
                onCheckedChange = onReminderChanged
            )
            Spacer(modifier = Modifier.height(10.dp))
            SwitchRow(
                label = stringResource(R.string.setting_sound),
                checked = state.soundCues,
                stateLabel = if (state.soundCues) onLabel else offLabel,
                onCheckedChange = onSoundChanged
            )
            Spacer(modifier = Modifier.height(10.dp))
            SwitchRow(
                label = stringResource(R.string.setting_motion),
                checked = state.reducedMotion,
                stateLabel = if (state.reducedMotion) onLabel else offLabel,
                onCheckedChange = onMotionChanged
            )

            Spacer(modifier = Modifier.height(22.dp))

            SectionHeader(title = stringResource(R.string.goals_per_day))

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                GameConfig.GOALS_PER_DAY_OPTIONS.forEach { option ->
                    GoalCountChip(
                        count = option,
                        selected = option == state.goalsPerDay,
                        onClick = { onGoalsPerDayChanged(option) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            SecondaryButton(
                text = stringResource(R.string.reset_progress),
                onClick = onResetRequested,
                borderColor = TigerTerracotta,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(10.dp))
            SecondaryButton(
                text = stringResource(R.string.menu),
                onClick = onMenu,
                iconRes = R.drawable.ic_back,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = stringResource(R.string.app_footer),
                style = MaterialTheme.typography.labelSmall,
                color = TextSecondary,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun SwitchRow(
    label: String,
    checked: Boolean,
    stateLabel: String,
    onCheckedChange: (Boolean) -> Unit
) {
    GlassCard(
        radius = 18,
        borderColor = LanternGold.copy(alpha = 0.22f),
        modifier = Modifier.fillMaxWidth().height(64.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .semantics { stateDescription = stateLabel }
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                color = TextPrimary,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = BackgroundBase,
                    checkedTrackColor = LanternGold,
                    uncheckedThumbColor = TextSecondary,
                    uncheckedTrackColor = SurfaceNight
                )
            )
        }
    }
}

@Composable
private fun GoalCountChip(
    count: Int,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (selected) LanternGold.copy(alpha = 0.18f) else SurfaceNight.copy(alpha = 0.8f),
        border = BorderStroke(
            1.dp,
            if (selected) LanternGold else NightBlue.copy(alpha = 0.6f)
        ),
        modifier = modifier.height(48.dp).clickable(onClick = onClick)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleMedium,
                color = if (selected) LanternGold else TextSecondary
            )
        }
    }
}
