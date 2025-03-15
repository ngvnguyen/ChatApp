package com.sf.chatapp.remote.model

import com.google.firebase.Timestamp

data class Conversation(
    val content:List<Content> = listOf(),
    val timestamp: Timestamp = Timestamp.now()
)
