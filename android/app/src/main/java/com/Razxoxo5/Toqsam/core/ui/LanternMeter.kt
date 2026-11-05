package com.Razxoxo5.Toqsam.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.stateDescription
import androidx.compose.ui.unit.dp
import com.Razxoxo5.Toqsam.R
import com.Razxoxo5.Toqsam.core.theme.LanternGold
import com.Razxoxo5.Toqsam.core.theme.NightBlue

@Composable
fun LanternMeter(
    total: Int,
    lit: Int,
    stateLabel: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .semantics {
                stateDescription = stateLabel
                liveRegion = LiveRegionMode.Polite
            }
    ) {
        repeat(total.coerceAtLeast(1)) { index ->
            LanternSlot(isLit = index < lit)
        }
    }
}

@Composable
private fun LanternSlot(isLit: Boolean) {
    val glowAlpha by animateFloatAsState(
        targetValue = if (isLit) 1f else 0.22f,
        animationSpec = tween(durationMillis = 360),
        label = "lanternAlpha"
    )
    val slotScale by animateFloatAsState(
        targetValue = if (isLit) 1.08f else 0.9f,
        animationSpec = spring(dampingRatio = 0.55f, stiffness = 320f),
        label = "lanternScale"
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(52.dp)
    ) {
        if (isLit) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(LanternGold.copy(alpha = 0.45f), Color.Transparent)
                        ),
                        CircleShape
                    )
            )
        } else {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(NightBlue.copy(alpha = 0.5f), CircleShape)
            )
        }
        Image(
            painter = painterResource(R.drawable.sprite_2),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .size(40.dp)
                .scale(slotScale)
                .alpha(glowAlpha)
        )
    }
}
