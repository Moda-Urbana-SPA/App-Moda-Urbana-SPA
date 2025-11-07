package com.example.modaurbanaprototipoapp.ui.theme

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
    primary = White,
    onPrimary = Black,
    primaryContainer = DarkGrey,
    onPrimaryContainer = White,

    secondary = LightGrey,
    onSecondary = Black,

    tertiary = Grey,
    onTertiary = White,

    background = Black,
    onBackground = White,
    surface = DarkGrey,
    onSurface = White,
    surfaceVariant = Grey,
    onSurfaceVariant = OffWhite,

    error = Color(0xFFCF6679),
    errorContainer = Color(0xFF4E0000),
    onErrorContainer = White
)

private val LightColorScheme = lightColorScheme(
    primary = Black,
    onPrimary = White,
    primaryContainer = OffWhite,
    onPrimaryContainer = Black,

    secondary = Grey,
    onSecondary = White,

    tertiary = LightGrey,
    onTertiary = Black,

    background = White,
    onBackground = Black,
    surface = White,
    onSurface = Black,
    surfaceVariant = OffWhite,
    onSurfaceVariant = Grey,

    error = Color(0xFFB3261E),
    errorContainer = Color(0xFFFFDAD4),
    onErrorContainer = Black
)

@Composable
fun ModaUrbanaPrototipoAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
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
