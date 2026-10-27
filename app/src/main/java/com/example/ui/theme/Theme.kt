package com.example.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
  primary = BrandGreenPrimary,
  onPrimary = MountainWhite,
  primaryContainer = BrandGreenContainer,
  onPrimaryContainer = BrandGreenDark,
  secondary = BrandGreenLight,
  onSecondary = MountainWhite,
  secondaryContainer = BrandGreenSurface,
  onSecondaryContainer = BrandGreenDark,
  tertiary = RewardGold,
  onTertiary = MountainWhite,
  tertiaryContainer = RewardGoldContainer,
  onTertiaryContainer = BrandGreenDark,
  background = MountainSnowBackground,
  onBackground = TextPrimaryDark,
  surface = MountainCardSurface,
  onSurface = TextPrimaryDark,
  surfaceVariant = BrandGreenSurface,
  onSurfaceVariant = TextSecondarySlate,
  outline = MountainBorder,
  outlineVariant = BrandGreenContainer,
  error = ErrorRed,
  errorContainer = ErrorContainer,
  onError = MountainWhite,
)

private val DarkColorScheme = darkColorScheme(
  primary = BrandGreenAccent,
  onPrimary = BrandGreenDark,
  primaryContainer = BrandGreenDark,
  onPrimaryContainer = BrandGreenContainer,
  secondary = BrandGreenLight,
  onSecondary = MountainWhite,
  secondaryContainer = BrandGreenDark,
  onSecondaryContainer = BrandGreenContainer,
  tertiary = RewardGold,
  onTertiary = TextPrimaryDark,
  background = TextPrimaryDark,
  onBackground = MountainWhite,
  surface = Color(0xFF1E293B),
  onSurface = MountainWhite,
  surfaceVariant = Color(0xFF334155),
  onSurfaceVariant = MountainWhite,
  outline = Color(0xFF475569),
  error = ErrorRed,
  onError = MountainWhite,
)

@Composable
fun ShahpurGoTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  val view = LocalView.current
  if (!view.isInEditMode) {
    SideEffect {
      val window = (view.context as? Activity)?.window
      window?.let {
        it.statusBarColor = BrandGreenPrimary.toArgb()
        WindowCompat.getInsetsController(it, view).isAppearanceLightStatusBars = false
      }
    }
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
