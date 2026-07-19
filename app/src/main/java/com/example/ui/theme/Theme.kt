package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme =
  darkColorScheme(
    primary = TarotGold, 
    secondary = TarotAccent, 
    tertiary = TarotGold,
    background = TarotDarkBg,
    surface = TarotCardBg,
    onPrimary = TarotDarkBg,
    onSecondary = Color.White,
    onTertiary = TarotDarkBg,
    onBackground = TarotTextPrimary,
    onSurface = TarotTextPrimary
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Dynamic color is available on Android 12+
  dynamicColor: Boolean = false, // Disable dynamic colors to enforce the mystical theme
  content: @Composable () -> Unit,
) {
  val colorScheme = DarkColorScheme // Always use dark mystical theme

  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
