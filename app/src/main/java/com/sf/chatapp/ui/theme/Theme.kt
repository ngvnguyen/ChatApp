package com.sf.chatapp.ui.theme
import android.app.Activity
import android.content.Context
import android.net.Uri
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.sf.chatapp.R
import com.sf.chatapp.utils.ColorSchemeState
import com.sf.chatapp.utils.LocalColorSchemeState
import com.sf.chatapp.utils.LocalToastManager
import com.sf.chatapp.utils.ToastManager

private val lightDefaultScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkDefaultScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val greenLightScheme = lightColorScheme(
    primary = Color(0xFF45D958),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFB9F0B8),
    onPrimaryContainer = Color(0xFF1F5027),
    secondary = Color(0xFF516350),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFD4E8D0),
    onSecondaryContainer = Color(0xFF3A4B39),
    tertiary = Color(0xFF39656C),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFBCEBF2),
    onTertiaryContainer = Color(0xFF1F4D54),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF93000A),
    background = Color(0xFFF7FBF2),
    onBackground = Color(0xFF181D18),
    surface = Color(0xFFF7FBF2),
    onSurface = Color(0xFF181D18),
    surfaceVariant = Color(0xFFDEE5D9),
    onSurfaceVariant = Color(0xFF424940),
    outline = Color(0xFF72796F),
    outlineVariant = Color(0xFFC2C9BD),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF2D322C),
    inverseOnSurface = Color(0xFFEEF2E9),
    inversePrimary = Color(0xFF9DD49E),
    surfaceDim = Color(0xFFD7DBD3),
    surfaceBright = Color(0xFFF7FBF2),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF1F5EC),
    surfaceContainer = Color(0xFFEBEFE6),
    surfaceContainerHigh = Color(0xFFE6E9E1),
    surfaceContainerHighest = Color(0xFFE0E4DB)
)

private val greenDarkScheme = darkColorScheme(
    primary = Color(0xFF40FA43),
    onPrimary = Color(0xFF033912),
    primaryContainer = Color(0xFF1F5027),
    onPrimaryContainer = Color(0xFFB9F0B8),
    secondary = Color(0xFFB8CCB5),
    onSecondary = Color(0xFF243424),
    secondaryContainer = Color(0xFF3A4B39),
    onSecondaryContainer = Color(0xFFD4E8D0),
    tertiary = Color(0xFFA1CED6),
    onTertiary = Color(0xFF00363C),
    tertiaryContainer = Color(0xFF1F4D54),
    onTertiaryContainer = Color(0xFFBCEBF2),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF101510),
    onBackground = Color(0xFFE0E4DB),
    surface = Color(0xFF101510),
    onSurface = Color(0xFFE0E4DB),
    surfaceVariant = Color(0xFF424940),
    onSurfaceVariant = Color(0xFFC2C9BD),
    outline = Color(0xFF8C9389),
    outlineVariant = Color(0xFF424940),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFE0E4DB),
    inverseOnSurface = Color(0xFF2D322C),
    inversePrimary = Color(0xFF37693D),
    surfaceDim = Color(0xFF101510),
    surfaceBright = Color(0xFF363A35),
    surfaceContainerLowest = Color(0xFF0B0F0B),
    surfaceContainerLow = Color(0xFF181D18),
    surfaceContainer = Color(0xFF1C211C),
    surfaceContainerHigh = Color(0xFF272B26),
    surfaceContainerHighest = Color(0xFF313630)
)

private val purpleLightScheme = lightColorScheme(
    primary = Color(0xFF8E2EEE),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFEFDBFF),
    onPrimaryContainer = Color(0xFF553B71),
    secondary = Color(0xFF655A6F),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFEDDDF6),
    onSecondaryContainer = Color(0xFF4D4357),
    tertiary = Color(0xFF805158),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFD9DD),
    onTertiaryContainer = Color(0xFF653A41),
    error = Color(0xFFBA1A1A),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD6),
    onErrorContainer = Color(0xFF93000A),
    background = Color(0xFFFFF7FF),
    onBackground = Color(0xFF1E1A20),
    surface = Color(0xFFFFF7FF),
    onSurface = Color(0xFF1E1A20),
    surfaceVariant = Color(0xFFE9DFEB),
    onSurfaceVariant = Color(0xFF4A454E),
    outline = Color(0xFF7B757E),
    outlineVariant = Color(0xFFCCC4CE),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFF332F35),
    inverseOnSurface = Color(0xFFF6EEF6),
    inversePrimary = Color(0xFFDAB9F9),
    surfaceDim = Color(0xFFDFD8DF),
    surfaceBright = Color(0xFFFFF7FF),
    surfaceContainerLowest = Color(0xFFFFFFFF),
    surfaceContainerLow = Color(0xFFF9F1F9),
    surfaceContainer = Color(0xFFF3EBF3),
    surfaceContainerHigh = Color(0xFFEEE6EE),
    surfaceContainerHighest = Color(0xFFE8E0E8)
)


private val purpleDarkScheme = darkColorScheme(
    primary = Color(0xFF9225F6),
    onPrimary = Color(0xFF3E2459),
    primaryContainer = Color(0xFF553B71),
    onPrimaryContainer = Color(0xFFEFDBFF),
    secondary = Color(0xFFD0C1DA),
    onSecondary = Color(0xFF362C3F),
    secondaryContainer = Color(0xFF4D4357),
    onSecondaryContainer = Color(0xFFEDDDF6),
    tertiary = Color(0xFFF3B7BE),
    onTertiary = Color(0xFF4B252B),
    tertiaryContainer = Color(0xFF653A41),
    onTertiaryContainer = Color(0xFFFFD9DD),
    error = Color(0xFFFFB4AB),
    onError = Color(0xFF690005),
    errorContainer = Color(0xFF93000A),
    onErrorContainer = Color(0xFFFFDAD6),
    background = Color(0xFF151218),
    onBackground = Color(0xFFE8E0E8),
    surface = Color(0xFF151218),
    onSurface = Color(0xFFE8E0E8),
    surfaceVariant = Color(0xFF4A454E),
    onSurfaceVariant = Color(0xFFCCC4CE),
    outline = Color(0xFF968E98),
    outlineVariant = Color(0xFF4A454E),
    scrim = Color(0xFF000000),
    inverseSurface = Color(0xFFE8E0E8),
    inverseOnSurface = Color(0xFF332F35),
    inversePrimary = Color(0xFF6E528A),
    surfaceDim = Color(0xFF151218),
    surfaceBright = Color(0xFF3C383E),
    surfaceContainerLowest = Color(0xFF100D12),
    surfaceContainerLow = Color(0xFF1E1A20),
    surfaceContainer = Color(0xFF221E24),
    surfaceContainerHigh = Color(0xFF2C292E),
    surfaceContainerHighest = Color(0xFF373339)
)

private val lightDefaultTheme = ColorSchemeState(
    id = 1,
    name = R.string.light,
    colorScheme = lightDefaultScheme
)

private val darkDefaultTheme = ColorSchemeState(
    id = 2,
    name = R.string.dark,
    colorScheme = darkDefaultScheme
)

private val greenLightTheme = ColorSchemeState(
    id = 3,
    name = R.string.green_light,
    colorScheme = greenLightScheme
)

private val greenDarkTheme = ColorSchemeState(
    id = 4,
    name = R.string.green_dark,
    colorScheme = greenDarkScheme
)

private val purpleLightTheme = ColorSchemeState(
    id = 5,
    name = R.string.purple_light,
    colorScheme = purpleLightScheme
)

private val purpleDarkTheme = ColorSchemeState(
    id = 6,
    name = R.string.purple_dark,
    colorScheme = purpleDarkScheme
)

val themes = mapOf(
    lightDefaultTheme.id to lightDefaultTheme,
    darkDefaultTheme.id to darkDefaultTheme,
    greenLightTheme.id to greenLightTheme,
    greenDarkTheme.id to greenDarkTheme,
    purpleLightTheme.id to purpleLightTheme,
    purpleDarkTheme.id to purpleDarkTheme
)

@Composable
fun ChatAppTheme(
    content: @Composable() () -> Unit
) {
    val context = LocalContext.current
    val isDarkTheme = isSystemInDarkTheme()
    val colorSchemeState = remember { mutableStateOf(
        context.getThemeColorSchemeState()?: getDefaultColorSchemeState(isDarkTheme)
    ) }


    val toastManager = ToastManager(LocalContext.current)
    CompositionLocalProvider(
        LocalToastManager provides toastManager,
        LocalColorSchemeState provides colorSchemeState
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            WrapperTheme(
                colorScheme = LocalColorSchemeState.current.value.colorScheme,
                content = content
            )
        }

    }


}

@Composable
fun WrapperTheme(
    colorScheme:ColorScheme,
    content: @Composable () -> Unit
){
    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content
    )
}

fun Context.getThemeColorSchemeState(): ColorSchemeState? {
    val theme = getSharedPreferences("theme", Activity.MODE_PRIVATE)
    val themeId = theme.getInt("themeId", 0)
    return themes[themeId]
}

fun Context.setDefaultColorScheme(localColorSchemeState: MutableState<ColorSchemeState>,isDarkTheme: Boolean){
    localColorSchemeState.value = if(isDarkTheme) darkDefaultTheme else lightDefaultTheme
    getSharedPreferences("theme",Activity.MODE_PRIVATE).edit().putInt("themeId",0).apply()
}

fun getDefaultColorSchemeState(isDarkTheme:Boolean): ColorSchemeState {
    return if(isDarkTheme) darkDefaultTheme else lightDefaultTheme
}

fun Context.setColorScheme(localColorSchemeState: MutableState<ColorSchemeState>, id:Int){
    themes[id]?.let {localColorSchemeState.value = it  }
    getSharedPreferences("theme",Activity.MODE_PRIVATE).edit().putInt("themeId",id).apply()
}