package com.sf.chatapp.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Part(
    @SerialName("text")
    val text: String=""
)