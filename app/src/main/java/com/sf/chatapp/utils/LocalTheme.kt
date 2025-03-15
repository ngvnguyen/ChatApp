package com.sf.chatapp.utils

import androidx.annotation.StringRes
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.compositionLocalOf

data class ColorSchemeState(
    val id:Int,
    @StringRes val name:Int,
    var colorScheme:ColorScheme
)

val LocalColorSchemeState = compositionLocalOf<MutableState<ColorSchemeState>> {
    error("not initialized yet")
}