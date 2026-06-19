package com.example.empty_activity.theme

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

private val DarkColorScheme =
    darkColorScheme(
        primary = Blue80,
        secondary = BlueGrey80,
        tertiary = Orange80,
        background = Color(0xFFFFFBFE),
        surface = Color(0xFFFFFBFE),
        onPrimary = Color(0xFF381E72),
        onSecondary = Color(0xFF332D41),
        onTertiary = Color(0xFF372E12),
        onBackground = Color(0xFFE6E1E5),
        onSurface = Color(0xFFE6E1E5),
    )

private val LightColorScheme = lightColorScheme(
    primary = Blue40,
    secondary = BlueGrey40,
    tertiary = Orange40,
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
)


@Composable fun EmptyActivityTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit,
)
{
    val colorScheme = when
    {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
        {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicDarkColorScheme(context)
        }

        darkTheme                                                      -> DarkColorScheme
        else                                                           -> DarkColorScheme
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
