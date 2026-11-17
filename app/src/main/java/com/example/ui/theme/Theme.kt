package com.example.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val ViceDarkColorScheme = darkColorScheme(
  primary = ViceFlamingo,
  onPrimary = Color.White,
  primaryContainer = ViceFlamingoContainer,
  onPrimaryContainer = Color(0xFFFFD8E6),
  secondary = ViceCyan,
  onSecondary = Color(0xFF002026),
  secondaryContainer = ViceCyanContainer,
  onSecondaryContainer = Color(0xFFB3F6FF),
  tertiary = ViceSunsetAmber,
  onTertiary = Color(0xFF2B1700),
  background = ViceObsidian,
  onBackground = ViceTextPrimary,
  surface = ViceSurface,
  onSurface = ViceTextPrimary,
  surfaceVariant = ViceSurfaceElevated,
  onSurfaceVariant = ViceTextSecondary,
  surfaceContainerLow = ViceSurface,
  surfaceContainer = ViceSurfaceElevated,
  surfaceContainerHigh = ViceSurfaceHighest,
  outline = ViceBorder,
  outlineVariant = Color(0xFF262242),
  error = ViceSunsetCoral,
)

private val ViceLightColorScheme = lightColorScheme(
  primary = DayPrimary,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFFFD9E4),
  onPrimaryContainer = Color(0xFF3E001B),
  secondary = DaySecondary,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFCFF6FF),
  onSecondaryContainer = Color(0xFF002028),
  tertiary = DayTertiary,
  onTertiary = Color.White,
  background = DayBackground,
  onBackground = DayOnBackground,
  surface = DaySurface,
  onSurface = DayOnBackground,
  surfaceVariant = DaySurfaceVariant,
  onSurfaceVariant = DayOnSurfaceVariant,
  surfaceContainerLow = DaySurface,
  surfaceContainer = Color(0xFFFFFFFF),
  surfaceContainerHigh = DaySurfaceVariant,
  outline = Color(0xFFD5CEE8),
  outlineVariant = Color(0xFFE5E0F2),
  error = Color(0xFFBA1A1A),
)

val ViceShapes = Shapes(
  small = RoundedCornerShape(12.dp),
  medium = RoundedCornerShape(20.dp),
  large = RoundedCornerShape(28.dp),
)

@Composable
fun ViceTimeTheme(
  darkTheme: Boolean = true,
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) ViceDarkColorScheme else ViceLightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    shapes = ViceShapes,
    content = content,
  )
}
