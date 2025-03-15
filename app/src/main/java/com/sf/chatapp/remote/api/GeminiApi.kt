package com.sf.chatapp.remote.api

import com.sf.chatapp.BuildConfig
import com.sf.chatapp.remote.model.GeminiResponse
import com.sf.chatapp.remote.model.RequestBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query


interface GeminiApi {
    @POST("v1beta/models/gemini-2.0-flash:generateContent")
    suspend fun sendMessage(
        @Body requestBody: RequestBody,
        @Query("key") key:String = BuildConfig.apiKey
    ): GeminiResponse

}