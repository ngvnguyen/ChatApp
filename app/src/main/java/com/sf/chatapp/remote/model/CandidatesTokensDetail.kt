package com.sf.chatapp.remote.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CandidatesTokensDetail(
    @SerialName("modality")
    val modality: String,
    @SerialName("tokenCount")
    val tokenCount: Int
)