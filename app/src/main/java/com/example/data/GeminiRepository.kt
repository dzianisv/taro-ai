package com.example.data

import android.graphics.Bitmap
import com.example.BuildConfig
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Url
import retrofit2.http.Header
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import okhttp3.OkHttpClient
import java.io.ByteArrayOutputStream
import java.util.concurrent.TimeUnit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import com.squareup.moshi.Moshi

private const val MODEL_NAME = "gemini-2.5-flash"

// Result of a free-text reading request. QuotaExceeded is a distinct sentinel so the
// ViewModel can surface a friendly message instead of silently falling back offline.
sealed class GeminiResult {
    data class Success(val text: String) : GeminiResult()
    object QuotaExceeded : GeminiResult()
}

// Result of a structured card-scan analysis request.
sealed class TarotAnalysisResult {
    data class Success(val reading: TarotReading) : TarotAnalysisResult()
    object QuotaExceeded : TarotAnalysisResult()
}

interface GeminiApiService {
    @POST("v1beta/models/$MODEL_NAME:generateContent")
    suspend fun generateContent(
        @Header("Authorization") authToken: String?,
        @Query("key") apiKey: String?,
        @Body request: GenerateContentRequest
    ): GenerateContentResponse

    @POST
    suspend fun generateContentDynamic(
        @Url url: String,
        @Header("Authorization") authToken: String?,
        @Query("key") apiKey: String?,
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

    private val moshi = Moshi.Builder().build()
    private val tarotReadingAdapter = moshi.adapter(TarotReading::class.java)

    private suspend fun getAuthHeader(): String? {
        return try {
            val user = FirebaseAuth.getInstance().currentUser
            val token = user?.getIdToken(false)?.await()?.token
            if (token != null) "Bearer $token" else null
        } catch (e: Exception) {
            null
        }
    }

    // The stored gateway URL is treated as a BASE; the model path is always appended here so
    // the user (or the default proxy) only ever needs to configure the base host.
    private fun buildGatewayUrl(base: String): String {
        val trimmed = base.trimEnd('/')
        return "$trimmed/v1beta/models/$MODEL_NAME:generateContent"
    }

    private fun cleanJsonBody(rawText: String): String {
        var text = rawText.trim()
        if (text.startsWith("```json")) {
            text = text.substringAfter("```json").substringBeforeLast("```").trim()
        } else if (text.startsWith("```")) {
            text = text.substringAfter("```").substringBeforeLast("```").trim()
        }
        return text
    }

    private fun Bitmap.toBase64Jpeg(): String {
        val outputStream = ByteArrayOutputStream()
        compress(Bitmap.CompressFormat.JPEG, 85, outputStream)
        return android.util.Base64.encodeToString(outputStream.toByteArray(), android.util.Base64.NO_WRAP)
    }

    private fun offlineTarotReading(): TarotReading {
        return TarotReading(
            cardName = "Mystery Card",
            orientation = "Upright",
            summary = "Offline reading — no gateway or API key is configured right now.",
            generalMeaning = LocalTarotInterpreter.generateInterpretation("Card Scan", listOf("Unknown Card")),
            advice = "Reflect on your own intuition while cosmic connectivity is restored.",
            warning = "This is a fallback reading; configure an API key or gateway in Settings for AI-powered card recognition.",
            luckyElements = listOf("Patience", "Reflection", "Trust")
        )
    }

    suspend fun generateReading(
        type: String,
        cards: List<String>,
        prompt: String,
        systemInstruction: String,
        customApiKey: String = "",
        customGatewayUrl: String = ""
    ): GeminiResult = withContext(Dispatchers.IO) {
        val apiKey = if (customApiKey.isNotEmpty()) customApiKey else BuildConfig.GEMINI_API_KEY
        val useGateway = customGatewayUrl.isNotEmpty()

        // If no API key is set AND no gateway URL is configured, we must fall back to the offline engine
        if (apiKey.isEmpty() && !useGateway) {
            return@withContext GeminiResult.Success(LocalTarotInterpreter.generateInterpretation(type, cards))
        }

        val request = GenerateContentRequest(
            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
            systemInstruction = Content(parts = listOf(Part(text = systemInstruction)))
        )
        try {
            val response = if (useGateway) {
                // The proxy holds the Gemini key server-side; never send ?key= to it.
                RetrofitClient.service.generateContentDynamic(buildGatewayUrl(customGatewayUrl), getAuthHeader(), null, request)
            } else {
                RetrofitClient.service.generateContent(null, apiKey, request)
            }
            val text = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: LocalTarotInterpreter.generateInterpretation(type, cards)
            GeminiResult.Success(text)
        } catch (e: HttpException) {
            if (e.code() == 429) {
                GeminiResult.QuotaExceeded
            } else {
                // Fall back to high quality local interpreter if API limit or error occurs
                GeminiResult.Success(LocalTarotInterpreter.generateInterpretation(type, cards))
            }
        } catch (e: Exception) {
            // Fall back to high quality local interpreter if API limit or error occurs
            GeminiResult.Success(LocalTarotInterpreter.generateInterpretation(type, cards))
        }
    }

    suspend fun analyzeScannedCard(
        imageBase64: String,
        mimeType: String,
        prompt: String,
        systemInstruction: String,
        customApiKey: String = "",
        customGatewayUrl: String = ""
    ): GeminiResult = withContext(Dispatchers.IO) {
        val apiKey = if (customApiKey.isNotEmpty()) customApiKey else BuildConfig.GEMINI_API_KEY
        val useGateway = customGatewayUrl.isNotEmpty()

        // If no API key is set AND no gateway URL is configured, fall back to offline message
        if (apiKey.isEmpty() && !useGateway) {
            return@withContext GeminiResult.Success(
                "🔮 *Ethereal Connection Mode (Offline Fallback)*\n\nNo API Key or Gateway is configured, but your physical card scan was captured successfully. Ground your mind and look at the image you captured—what thoughts or feelings arise in your subconscious? The answer lies within."
            )
        }

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
            val response = if (useGateway) {
                RetrofitClient.service.generateContentDynamic(buildGatewayUrl(customGatewayUrl), getAuthHeader(), null, request)
            } else {
                RetrofitClient.service.generateContent(null, apiKey, request)
            }
            val text = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: "🔮 *Ethereal Connection Mode (Offline Fallback)*\n\nThe spirits saw your scanned card but their message was lost in transmission. Take a moment to reflect on the card's visual symbols."
            GeminiResult.Success(text)
        } catch (e: HttpException) {
            if (e.code() == 429) {
                GeminiResult.QuotaExceeded
            } else {
                GeminiResult.Success(
                    "🔮 *Ethereal Connection Mode (Offline Fallback)*\n\nWe successfully captured your scanned card image! However, the cosmic connection to the server was temporarily interrupted (HTTP ${e.code()}). Connect with the card's imagery and colors directly—your intuition is your strongest guide."
                )
            }
        } catch (e: Exception) {
            GeminiResult.Success(
                "🔮 *Ethereal Connection Mode (Offline Fallback)*\n\nWe successfully captured your scanned card image! However, the cosmic connection to the server was temporarily interrupted (${e.localizedMessage}). Connect with the card's imagery and colors directly—your intuition is your strongest guide."
            )
        }
    }

    // Structured card analysis, ported from TaroAiScanner's GeminiTarotService.analyzeTarotCard.
    // Requests JSON output (responseMimeType=application/json) matching the exact TarotReading schema,
    // routed through the secure proxy with a Firebase Bearer token (proxy holds the Gemini key).
    suspend fun analyzeTarotCardStructured(
        bitmap: Bitmap,
        promptContext: String = "Single Card Draw",
        customGatewayUrl: String = ""
    ): TarotAnalysisResult = withContext(Dispatchers.IO) {
        val useGateway = customGatewayUrl.isNotEmpty()
        if (!useGateway) {
            return@withContext TarotAnalysisResult.Success(offlineTarotReading())
        }

        val prompt = """
            You are a wise and highly intuitive, professional Tarot Card Reader.
            Analyze the physical tarot card shown in the uploaded image.
            Identify the Tarot card name and whether it is oriented Upright or Reversed.
            Context of the reading: $promptContext.

            Return your complete response strictly as a JSON object matching this exact schema:
            {
              "cardName": "Name of the card",
              "orientation": "Upright" or "Reversed",
              "summary": "A beautiful 1-sentence summary of the card's energy today",
              "generalMeaning": "Detailed paragraph exploring the general interpretation and psychological/spiritual archetype of this card",
              "advice": "Actionable positive guidance/steps for the seeker based on this card",
              "warning": "A gentle warning or pitfall to avoid under this card's energy",
              "luckyElements": ["A lucky color", "A lucky hour or time", "A key number", "An aligned element or astrological sign"]
            }
            Do not include any other markdown, text or explanation outside the JSON. Return only valid raw JSON.
        """.trimIndent()

        val request = GenerateContentRequest(
            contents = listOf(
                Content(
                    parts = listOf(
                        Part(text = prompt),
                        Part(inlineData = InlineData(mimeType = "image/jpeg", data = bitmap.toBase64Jpeg()))
                    )
                )
            ),
            generationConfig = GenerationConfig(responseMimeType = "application/json", temperature = 0.7f)
        )

        try {
            val response = RetrofitClient.service.generateContentDynamic(buildGatewayUrl(customGatewayUrl), getAuthHeader(), null, request)
            val text = response.candidates.firstOrNull()?.content?.parts?.firstOrNull()?.text
                ?: return@withContext TarotAnalysisResult.Success(offlineTarotReading())
            val reading = tarotReadingAdapter.fromJson(cleanJsonBody(text))
                ?: return@withContext TarotAnalysisResult.Success(offlineTarotReading())
            TarotAnalysisResult.Success(reading)
        } catch (e: HttpException) {
            if (e.code() == 429) {
                TarotAnalysisResult.QuotaExceeded
            } else {
                TarotAnalysisResult.Success(offlineTarotReading())
            }
        } catch (e: Exception) {
            TarotAnalysisResult.Success(offlineTarotReading())
        }
    }
}
