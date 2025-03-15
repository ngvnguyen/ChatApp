package com.sf.chatapp.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Candidate(
    @SerialName("avgLogprobs")
    val avgLogprobs: Double,
    @SerialName("content")
    val content: Content,
    @SerialName("finishReason")
    val finishReason: String
)