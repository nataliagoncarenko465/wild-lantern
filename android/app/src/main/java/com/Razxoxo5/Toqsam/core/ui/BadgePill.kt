package com.Razxoxo5.Toqsam.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.Razxoxo5.Toqsam.core.theme.LanternGold
import com.Razxoxo5.Toqsam.core.theme.SurfaceNight
import com.Razxoxo5.Toqsam.core.theme.TextPrimary

@Composable
fun BadgePill(
    iconRes: Int,
    text: String,
    modifier: Modifier = Modifier,
    iconDescription: String? = null,
    iconSize: Int = 18
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = SurfaceNight.copy(alpha = 0.9f),
        border = BorderStroke(1.dp, LanternGold.copy(alpha = 0.45f)),
        modifier = modifier.height(36.dp).semantics { liveRegion = LiveRegionMode.Polite }
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Image(
                painter = painterResource(iconRes),
                contentDescription = iconDescription,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(iconSize.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = TextPrimary
            )
        }
    }
}
