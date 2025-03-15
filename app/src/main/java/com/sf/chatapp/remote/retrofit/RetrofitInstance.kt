package com.sf.chatapp.remote.retrofit

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.sf.chatapp.remote.api.GeminiApi
import com.sf.chatapp.utils.Constraint
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit

class RetrofitInstance {
    val httpInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val httpClient = OkHttpClient.Builder()
        .addInterceptor(httpInterceptor)
        .build()
    val json = Json{
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    private val retrofit = Retrofit.Builder()
        .baseUrl(Constraint.GEMINI_BASE_URL)
        .client(httpClient)
        .addConverterFactory(json.asConverterFactory(("application/json").toMediaType()))
        .build()

    val geminiApi: GeminiApi by lazy{
        retrofit.create(GeminiApi::class.java)
    }
}