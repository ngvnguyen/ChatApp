package com.sf.chatapp.remote.repository

import com.sf.chatapp.remote.api.GeminiApi
import com.sf.chatapp.remote.model.GeminiResponse
import com.sf.chatapp.remote.model.RequestBody

class GeminiRepositoryImpl(private val geminiApi: GeminiApi): GeminiRepository {
    override suspend fun sendMessage(requestBody: RequestBody): GeminiResponse {
        return geminiApi.sendMessage(requestBody)
    }
}