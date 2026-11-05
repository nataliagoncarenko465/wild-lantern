package com.Razxoxo5.Toqsam.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val WildLanternColors = darkColorScheme(
    primary = LanternGold,
    onPrimary = BackgroundBase,
    primaryContainer = LanternGoldDark,
    onPrimaryContainer = TextPrimary,
    secondary = NightBlue,
    onSecondary = TextPrimary,
    secondaryContainer = SurfaceElevated,
    onSecondaryContainer = TextSecondary,
    tertiary = TigerTerracotta,
    onTertiary = BackgroundBase,
    tertiaryContainer = LanternGoldLight,
    onTertiaryContainer = BackgroundBase,
    background = BackgroundBase,
    onBackground = TextPrimary,
    surface = SurfaceNight,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = NightBlue,
    outlineVariant = Hairline,
    error = TigerTerracotta,
    onError = BackgroundBase,
    scrim = BackgroundShadow
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WildLanternColors,
        typography = WildLanternTypography,
        content = content
    )
}
