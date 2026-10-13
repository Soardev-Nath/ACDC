package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
  primary = WhatsAppLightGreen,
  secondary = WhatsAppTeal,
  tertiary = AcdaGold,
  background = WhatsAppBackgroundDark,
  surface = Color(0xFF1F2C34),
  onPrimary = Color.Black,
  onSecondary = Color.White,
  onBackground = Color(0xFFE9EDEF),
  onSurface = Color(0xFFE9EDEF)
)

private val LightColorScheme = lightColorScheme(
  primary = WhatsAppPrimary,
  secondary = WhatsAppTeal,
  tertiary = AcdaGold,
  background = AcdaSurface,
  surface = Color.White,
  onPrimary = Color.White,
  onSecondary = Color.White,
  onBackground = Color(0xFF111827),
  onSurface = Color(0xFF111827)
)

@Composable
fun AcdaTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false, // Keep WhatsApp/ACDA branding consistent
  content: @Composable () -> Unit
) {
  val colorScheme = when {
    dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
      val context = LocalContext.current
      if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
    }
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}
