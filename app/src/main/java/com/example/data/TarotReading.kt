package com.example.data

import com.squareup.moshi.JsonClass

// Structured Tarot card analysis result, ported from the TaroAiScanner app's
// GeminiTarotService/TarotReadingResponse. Fields match exactly so the same
// Gemini JSON schema/prompt can be reused.
@JsonClass(generateAdapter = true)
data class TarotReading(
    val cardName: String,
    val orientation: String, // "Upright" or "Reversed"
    val summary: String,
    val generalMeaning: String,
    val advice: String,
    val warning: String,
    val luckyElements: List<String>
)

// Serializes a structured reading into the free-text format used by ReadingEntity
// so the existing Room history keeps working without a schema change.
fun TarotReading.toInterpretationText(): String {
    return buildString {
        appendLine(summary)
        appendLine()
        appendLine("General Meaning: $generalMeaning")
        appendLine()
        appendLine("Advice: $advice")
        appendLine()
        appendLine("Warning: $warning")
        appendLine()
        appendLine("Lucky Elements: ${luckyElements.joinToString(", ")}")
    }.trim()
}
