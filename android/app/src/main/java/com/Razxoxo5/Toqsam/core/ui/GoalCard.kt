package com.Razxoxo5.Toqsam.core.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Razxoxo5.Toqsam.R
import com.Razxoxo5.Toqsam.core.theme.BackgroundBase
import com.Razxoxo5.Toqsam.core.theme.LanternGold
import com.Razxoxo5.Toqsam.core.theme.SurfaceElevated
import com.Razxoxo5.Toqsam.core.theme.SurfaceNight
import com.Razxoxo5.Toqsam.core.theme.TextPrimary
import com.Razxoxo5.Toqsam.core.theme.TextSecondary
import com.Razxoxo5.Toqsam.core.theme.TigerTerracotta
import com.Razxoxo5.Toqsam.domain.model.Goal

@Composable
fun GoalCard(
    goal: Goal,
    onToggle: () -> Unit,
    stateLabel: String,
    modifier: Modifier = Modifier
) {
    val borderColor by animateColorAsState(
        targetValue = when {
            goal.completed -> TigerTerracotta
            goal.isChallenge -> TigerTerracotta.copy(alpha = 0.6f)
            else -> LanternGold.copy(alpha = 0.28f)
        },
        label = "goalBorder"
    )
    val checkScale by animateFloatAsState(
        targetValue = if (goal.completed) 1f else 0.86f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 320f),
        label = "goalCheck"
    )

    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.Transparent,
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
            .fillMaxWidth()
            .height(76.dp)
            .semantics { stateDescription = stateLabel }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(listOf(SurfaceNight, SurfaceElevated)),
                    RoundedCornerShape(18.dp)
                )
                .padding(horizontal = 12.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(48.dp)
                    .clickable(onClick = onToggle)
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(42.dp)
                        .scale(checkScale)
                        .background(
                            if (goal.completed) LanternGold else Color.Transparent,
                            CircleShape
                        )
                        .border(
                            width = 2.dp,
                            color = if (goal.completed) LanternGold else TextSecondary.copy(alpha = 0.5f),
                            shape = CircleShape
                        )
                ) {
                    if (goal.completed) {
                        Icon(
                            painter = painterResource(R.drawable.ic_check),
                            contentDescription = null,
                            tint = BackgroundBase,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = goal.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (goal.completed) TextPrimary.copy(alpha = 0.6f) else TextPrimary,
                    textDecoration = if (goal.completed) TextDecoration.LineThrough else null,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = goal.category,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (goal.isChallenge) TigerTerracotta else TextSecondary,
                    fontSize = 10.sp,
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.sprite_5),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = goal.tokens.toString(),
                    style = MaterialTheme.typography.labelLarge,
                    color = LanternGold
                )
            }
        }
    }
}
