package com.sf.chatapp.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class RequestBody(
    val contents: List<Content>
){
    companion object{
        fun builder(messages:List<Content>,message: String): RequestBody{
            val part = Part(message)
            val content = Content(parts = listOf(part))
            return RequestBody(messages + content)
        }
    }
}
