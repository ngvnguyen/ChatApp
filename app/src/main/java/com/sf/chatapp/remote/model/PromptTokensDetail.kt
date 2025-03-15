package com.sf.chatapp.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PromptTokensDetail(
    @SerialName("modality")
    val modality: String,
    @SerialName("tokenCount")
    val tokenCount: Int
)