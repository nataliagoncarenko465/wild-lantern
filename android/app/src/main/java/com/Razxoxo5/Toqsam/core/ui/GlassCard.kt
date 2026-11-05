package com.Razxoxo5.Toqsam.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.Razxoxo5.Toqsam.core.theme.Hairline
import com.Razxoxo5.Toqsam.core.theme.SurfaceNight

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    radius: Int = 20,
    borderColor: Color = Hairline,
    containerColor: Color = SurfaceNight.copy(alpha = 0.9f),
    elevation: Int = 6,
    content: @Composable BoxScope.() -> Unit
) {
    Card(
        shape = RoundedCornerShape(radius.dp),
        colors = CardDefaults.cardColors(containerColor = containerColor),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation.dp),
        border = BorderStroke(1.dp, borderColor),
        modifier = modifier
    ) {
        Box(content = content)
    }
}
