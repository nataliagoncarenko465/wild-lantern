package com.Razxoxo5.Toqsam.presentation.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.Razxoxo5.Toqsam.R
import com.Razxoxo5.Toqsam.core.theme.LanternGold
import com.Razxoxo5.Toqsam.core.theme.SurfaceNight
import com.Razxoxo5.Toqsam.core.theme.TextPrimary
import com.Razxoxo5.Toqsam.core.theme.TextSecondary

@Composable
fun SceneDialog(
    sceneName: String,
    condition: String,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(22.dp),
        containerColor = SurfaceNight,
        titleContentColor = TextPrimary,
        textContentColor = TextSecondary,
        icon = {
            Image(
                painter = painterResource(R.drawable.sprite_2),
                contentDescription = sceneName,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(96.dp)
            )
        },
        title = {
            Text(
                text = sceneName,
                style = MaterialTheme.typography.titleMedium,
                color = LanternGold
            )
        },
        text = {
            Text(
                text = condition,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.close),
                    style = MaterialTheme.typography.labelLarge,
                    color = LanternGold
                )
            }
        }
    )
}
