package com.example.data

import com.example.BuildConfig
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url
import retrofit2.http.Header
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

interface GeminiApiService {
    @POST("v1beta/models/gemini-3.5-flash:generateContent")
    suspend fun generateContent(
        @Header("Authorization") authToken: String?,
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse

    @POST
    suspend fun generateContentDynamic(
        @Url url: String,
        @Header("Authorization") authToken: String?,
        @Query("key") apiKey: String,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse
}

object RetrofitClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val service: GeminiApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create(GeminiApiService::class.java)
    }
}

class GeminiRepository {
    private suspend fun getAuthHeader(): String? {
        return try {
            val user = FirebaseAuth.getInstance().currentUser
            val tokenResult = user?.getIdToken(false)?.await()
            val token = tokenResult?.token
            if (token != null) "Bearer $token" else null
        } catch (e: Exception) {
            null
        }
    }

    suspend fun generateReading(
        type: String,
        cards: List<String>,
        prompt: String,
        systemInstruction: String,
        customApiKey: String = "",
        customGatewayUrl: String = ""
    ): String = withContext(Dispatchers.IO) {
        val apiKey = if (customApiKey.isNotEmpty()) customApiKey else BuildConfig.GEMINI_API_KEY
        
        // If no API key is set AND no gateway URL is configured, we must fall back to the offline engine
        if (apiKey.isEmpty() && customGatewayUrl.isEmpty()) {
            return@withContext LocalTarotInterpreter.generateInterpretation(type, cards)
        }

        val authHeader = getAuthHeader()
        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            systemInstruction = Content(parts = listOf(Part(text = systemInstruction)))
        )
        try {
            val response = if (customGatewayUrl.isNotEmpty()) {
                RetrofitClient.service.generateContentDynamic(customGatewayUrl, authHeader, apiKey, request)
            } else {
                RetrofitClient.service.generateContent(authHeader, apiKey, request)
            }
            response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: LocalTarotInterpreter.generateInterpretation(type, cards)
        } catch (e: Exception) {
            // Fall back to high quality local interpreter if API limit or error occurs
            LocalTarotInterpreter.generateInterpretation(type, cards)
        }
    }

    suspend fun analyzeScannedCard(
        imageBase64: String,
        mimeType: String,
        prompt: String,
        systemInstruction: String,
        customApiKey: String = "",
        customGatewayUrl: String = ""
    ): String = withContext(Dispatchers.IO) {
        val apiKey = if (customApiKey.isNotEmpty()) customApiKey else BuildConfig.GEMINI_API_KEY
        
        // If no API key is set AND no gateway URL is configured, fall back to offline message
        if (apiKey.isEmpty() && customGatewayUrl.isEmpty()) {
            return@withContext "🔮 *Ethereal Connection Mode (Offline Fallback)*\n\nNo API Key or Gateway is configured, but your physical card scan was captured successfully. Ground your mind and look at the image you captured—what thoughts or feelings arise in your subconscious? The answer lies within."
        }

        val authHeader = getAuthHeader()
        val request = GenerateContentRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt),
                        Part(inlineData = InlineData(mimeType = mimeType, data = imageBase64))
                    )
                )
            ),
            systemInstruction = Content(parts = listOf(Part(text = systemInstruction)))
        )
        try {
            val response = if (customGatewayUrl.isNotEmpty()) {
                RetrofitClient.service.generateContentDynamic(customGatewayUrl, authHeader, apiKey, request)
            } else {
                RetrofitClient.service.generateContent(authHeader, apiKey, request)
            }
            response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text 
                ?: "🔮 *Ethereal Connection Mode (Offline Fallback)*\n\nThe spirits saw your scanned card but their message was lost in transmission. Take a moment to reflect on the card's visual symbols."
        } catch (e: Exception) {
            "🔮 *Ethereal Connection Mode (Offline Fallback)*\n\nWe successfully captured your scanned card image! However, the cosmic connection to the server was temporarily interrupted (${e.localizedMessage}). Connect with the card's imagery and colors directly—your intuition is your strongest guide."
        }
    }
}
