package com.sf.chatapp.utils

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf

data class Session(
    val hasSession:Boolean
)

val LocalSession = compositionLocalOf {
    mutableStateOf(Session(false))
}