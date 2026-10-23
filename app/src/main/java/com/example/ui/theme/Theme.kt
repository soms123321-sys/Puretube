package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = PureTubeRedBright,
    onPrimary = Color.White,
    primaryContainer = PureTubeRedDark,
    onPrimaryContainer = Color.White,
    secondary = PureTubeTextPrimary,
    onSecondary = PureTubeDarkBg,
    secondaryContainer = PureTubeSurfaceVariant,
    onSecondaryContainer = PureTubeTextPrimary,
    tertiary = AdBlockGreen,
    onTertiary = Color.Black,
    tertiaryContainer = AdBlockBadgeBg,
    onTertiaryContainer = AdBlockGreen,
    background = PureTubeDarkBg,
    onBackground = PureTubeTextPrimary,
    surface = PureTubeSurface,
    onSurface = PureTubeTextPrimary,
    surfaceVariant = PureTubeSurfaceVariant,
    onSurfaceVariant = PureTubeTextSecondary,
    outline = PureTubeOutline,
    outlineVariant = PureTubeOutline
)

private val LightColorScheme = DarkColorScheme // YouTube-style Ad-Free experience is best enjoyed in sleek dark theme by default

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
