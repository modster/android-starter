package com.greeffer.xcam.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.example.empty_activity.theme.Background
import com.example.empty_activity.theme.Error
import com.example.empty_activity.theme.ErrorContainer
import com.example.empty_activity.theme.InverseOnSurface
import com.example.empty_activity.theme.InversePrimary
import com.example.empty_activity.theme.InverseSurface
import com.example.empty_activity.theme.OnBackground
import com.example.empty_activity.theme.OnError
import com.example.empty_activity.theme.OnErrorContainer
import com.example.empty_activity.theme.OnPrimary
import com.example.empty_activity.theme.OnPrimaryContainer
import com.example.empty_activity.theme.OnSecondary
import com.example.empty_activity.theme.OnSecondaryContainer
import com.example.empty_activity.theme.OnSurface
import com.example.empty_activity.theme.OnSurfaceVariant
import com.example.empty_activity.theme.OnTertiary
import com.example.empty_activity.theme.OnTertiaryContainer
import com.example.empty_activity.theme.Outline
import com.example.empty_activity.theme.OutlineVariant
import com.example.empty_activity.theme.Primary
import com.example.empty_activity.theme.PrimaryContainer
import com.example.empty_activity.theme.Secondary
import com.example.empty_activity.theme.SecondaryContainer
import com.example.empty_activity.theme.Surface
import com.example.empty_activity.theme.SurfaceBright
import com.example.empty_activity.theme.SurfaceContainer
import com.example.empty_activity.theme.SurfaceContainerHigh
import com.example.empty_activity.theme.SurfaceContainerHighest
import com.example.empty_activity.theme.SurfaceContainerLow
import com.example.empty_activity.theme.SurfaceContainerLowest
import com.example.empty_activity.theme.SurfaceDim
import com.example.empty_activity.theme.SurfaceTint
import com.example.empty_activity.theme.SurfaceVariant
import com.example.empty_activity.theme.Tertiary
import com.example.empty_activity.theme.TertiaryContainer
import com.example.empty_activity.theme.Typography

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    inversePrimary = InversePrimary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceTint = SurfaceTint,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    outline = Outline,
    outlineVariant = OutlineVariant,
    surfaceDim = SurfaceDim,
    surfaceBright = SurfaceBright,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    inversePrimary = InversePrimary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    background = Background,
    onBackground = OnBackground,
    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,
    surfaceTint = SurfaceTint,
    inverseSurface = InverseSurface,
    inverseOnSurface = InverseOnSurface,
    error = Error,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
    outline = Outline,
    outlineVariant = OutlineVariant,
    surfaceDim = SurfaceDim,
    surfaceBright = SurfaceBright,
    surfaceContainerLowest = SurfaceContainerLowest,
    surfaceContainerLow = SurfaceContainerLow,
    surfaceContainer = SurfaceContainer,
    surfaceContainerHigh = SurfaceContainerHigh,
    surfaceContainerHighest = SurfaceContainerHighest
)


@Composable
fun XTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  // Disabled by default to keep brand palette from design-system.md.
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
)
{
    val colorScheme = when
    {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S ->
        {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme                                                      -> DarkColorScheme
        else                                                           -> LightColorScheme
    }
    val view = LocalView.current
    if (! view.isInEditMode)
    {
        SideEffect {
            val window = (view.context as Activity).window
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = ! darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
