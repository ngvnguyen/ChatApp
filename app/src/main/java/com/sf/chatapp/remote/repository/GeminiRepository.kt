package com.sf.chatapp.remote.repository

import com.sf.chatapp.remote.model.GeminiResponse
import com.sf.chatapp.remote.model.RequestBody

interface GeminiRepository {
    suspend fun sendMessage(requestBody: RequestBody): GeminiResponse
}