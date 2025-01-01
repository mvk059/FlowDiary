package fyi.manpreet.flowdiary.ui.theme

import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val Primary = Color(0xFF00419C)
val PrimaryContainer = Color(0xFF1F70F5)
val OnPrimary = Color(0xFFFFFFFF)
val OnPrimaryFixed = Color(0xFF001945)
val InversePrimary = Color(0xFFBAC6E9)

val Secondary = Color(0xFF3B4663)
val SecondaryContainer = Color(0xFF6B7796)

val Surface = Color(0xFFFFFFFF)
val SurfaceVariant = Color(0xFFE1E2EC)
val InverseOnSurface = Color(0xFFEEF0FF)
val OnSurface = Color(0xFF191A20)
val OnSurfaceVariant = Color(0xFF40434F)

val Background = Color(0xFFFCFDFE)

val Outline = Color(0xFF6C7085)
val OutlineVariant = Color(0xFFC1C3CE)

val OnError = Color(0xFFFFFFFF)
val ErrorContainer = Color(0xFFFFEDEC)
val OnErrorContainer = Color(0xFF680014)

val Secondary90 = Color(0xFFD9E2FF)
val Secondary95 = Color(0xFFEEF0FF)

internal val ColorScheme = lightColorScheme(
    primary = Primary,
    primaryContainer = PrimaryContainer,
    onPrimary = OnPrimary,
    inversePrimary = InversePrimary,
    secondary = Secondary,
    secondaryContainer = SecondaryContainer,
    background = Background,
    surface = Surface,
    surfaceVariant = SurfaceVariant,
    inverseOnSurface = InverseOnSurface,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurfaceVariant,
    outline = Outline,
    outlineVariant = OutlineVariant,
    onError = OnError,
    errorContainer = ErrorContainer,
    onErrorContainer = OnErrorContainer,
)
