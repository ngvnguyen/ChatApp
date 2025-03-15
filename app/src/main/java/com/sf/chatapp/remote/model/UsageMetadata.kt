package com.sf.chatapp.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UsageMetadata(
    @SerialName("candidatesTokenCount")
    val candidatesTokenCount: Int,
    @SerialName("candidatesTokensDetails")
    val candidatesTokensDetails: List<CandidatesTokensDetail>,
    @SerialName("promptTokenCount")
    val promptTokenCount: Int,
    @SerialName("promptTokensDetails")
    val promptTokensDetails: List<PromptTokensDetail>,
    @SerialName("totalTokenCount")
    val totalTokenCount: Int
)