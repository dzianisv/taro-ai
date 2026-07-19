package com.example.ui

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.TarotReading
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonGreen

// Ported from TaroAiScanner's ReadingResultScreen.kt: renders a structured card-scan
// reading (cardName/orientation/summary/generalMeaning/advice/warning/luckyElements).
@Composable
fun StructuredReadingContent(reading: TarotReading, bitmap: Bitmap?) {
    Column(verticalArrangement = Arrangement.spacedBy(24.dp)) {
        // Scanned Card Preview
        bitmap?.let {
            Card(
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp)
                    .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(20.dp))
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Your Tarot Card Scan",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                                )
                            )
                    )
                    Text(
                        text = "YOUR PHYSICAL DRAW",
                        color = Color(0xFFD4AF37),
                        fontWeight = FontWeight.Black,
                        fontSize = 11.sp,
                        letterSpacing = 1.5.sp,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    )
                }
            }
        }

        // Card Title & Orientation Badge
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = reading.cardName,
                    style = MaterialTheme.typography.headlineLarge.copy(
                        fontWeight = FontWeight.Black,
                        letterSpacing = (-0.5).sp
                    ),
                    color = Color(0xFFD4AF37),
                    modifier = Modifier.weight(1f)
                )

                val badgeColor = if (reading.orientation == "Upright") NeonGreen else NeonBlue
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(badgeColor.copy(alpha = 0.15f))
                        .border(1.dp, badgeColor, RoundedCornerShape(8.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = reading.orientation.uppercase(),
                        color = badgeColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            Text(
                text = reading.summary,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(top = 12.dp)
            )
        }

        HorizontalDivider(color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f))

        SectionCard(title = "GENERAL INTERPRETATION", icon = Icons.Default.MenuBook, iconTint = Color(0xFFD4AF37)) {
            Text(
                text = reading.generalMeaning,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp
            )
        }

        SectionCard(title = "COSMIC ADVICE", icon = Icons.Default.Directions, iconTint = NeonGreen) {
            Text(
                text = reading.advice,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp
            )
        }

        SectionCard(title = "PITFALLS TO AVOID", icon = Icons.Default.Info, iconTint = com.example.ui.theme.WarningRed) {
            Text(
                text = reading.warning,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium,
                lineHeight = 22.sp
            )
        }

        // Lucky Elements Segment (simple wrap using nested Rows, mirrors source's minimal FlowRow helper)
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "LUCKY ELEMENTS",
                color = Color(0xFFD4AF37),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                reading.luckyElements.forEach { element ->
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.8f))
                            .border(0.5.dp, Color(0xFFD4AF37).copy(alpha = 0.4f), RoundedCornerShape(30.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = element,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.6f))
            .border(0.5.dp, Color(0xFFD4AF37).copy(alpha = 0.15f), RoundedCornerShape(16.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconTint,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = title,
                    color = Color(0xFFD4AF37),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }
            content()
        }
    }
}
