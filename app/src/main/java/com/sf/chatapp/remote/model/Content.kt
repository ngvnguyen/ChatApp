package com.sf.chatapp.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Content(
    @SerialName("parts")
    val parts: List<Part> = listOf(),
    @SerialName("role")
    val role: String="user"
)