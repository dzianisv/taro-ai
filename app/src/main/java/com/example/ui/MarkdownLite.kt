package com.example.ui

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle

// Tiny markdown-lite renderer for reading bodies: `*text*` -> bold span, asterisks stripped.
// Deliberately minimal (no external markdown library) — only handles the one pattern our
// local interpreter and Gemini's free-text responses actually use.
fun String.toMysticAnnotatedString(boldWeight: FontWeight = FontWeight.Bold): AnnotatedString {
    val pattern = Regex("\\*(.+?)\\*")
    return buildAnnotatedString {
        var lastIndex = 0
        for (match in pattern.findAll(this@toMysticAnnotatedString)) {
            append(this@toMysticAnnotatedString.substring(lastIndex, match.range.first))
            withStyle(SpanStyle(fontWeight = boldWeight)) {
                append(match.groupValues[1])
            }
            lastIndex = match.range.last + 1
        }
        append(this@toMysticAnnotatedString.substring(lastIndex))
    }
}
