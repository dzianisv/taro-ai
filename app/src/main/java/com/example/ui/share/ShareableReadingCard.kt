package com.example.ui.share

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.NeonBlue
import com.example.ui.theme.NeonGreen
import com.example.ui.theme.TarotAccent
import com.example.ui.theme.TarotDarkBg
import com.example.ui.theme.TarotGold
import com.example.ui.theme.TarotTextPrimary
import com.example.ui.theme.TarotTextSecondary
import kotlin.random.Random

private data class StaticStar(val x: Float, val y: Float, val radius: Float, val alpha: Float)

/**
 * A static (NO infinite animation) 9:16 shareable card rendered offscreen and captured
 * to a bitmap for the viral share loop. Deliberately avoids CosmicBackground's
 * rememberInfiniteTransition since a single-frame capture needs a fixed frame.
 */
@Composable
fun ShareableReadingCard(
    cardName: String,
    orientation: String?,
    summaryLine: String,
    displayName: String,
    streak: Int,
    referralLink: String,
    modifier: Modifier = Modifier,
) {
    // Deterministic star field (fixed seed) so the capture is stable and needs no animation clock.
    val stars = remember {
        val rnd = Random(1337)
        List(40) {
            StaticStar(
                x = rnd.nextFloat(),
                y = rnd.nextFloat(),
                radius = 1f + rnd.nextFloat() * 2f,
                alpha = 0.25f + rnd.nextFloat() * 0.5f
            )
        }
    }

    Box(
        modifier = modifier
            .width(360.dp)
            .aspectRatio(9f / 16f)
            .background(
                Brush.verticalGradient(
                    colors = listOf(TarotDarkBg, TarotAccent.copy(alpha = 0.55f), TarotDarkBg)
                )
            )
    ) {
        // Static starfield — plain Canvas dots, no infinite transition.
        Canvas(modifier = Modifier.fillMaxSize()) {
            stars.forEach { star ->
                drawCircle(
                    color = TarotGold.copy(alpha = star.alpha),
                    radius = star.radius * density,
                    center = Offset(star.x * size.width, star.y * size.height)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "T A R O",
                    color = TarotGold,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    letterSpacing = 4.sp
                )
                Text(
                    text = "Pick a card. Know yourself.",
                    color = TarotTextSecondary,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            // Centering the reading block within its own weighted region (rather than
            // top-anchoring it under the header via SpaceBetween) avoids a large dead
            // gap of empty gradient for short readings — the card stays balanced at 9:16.
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp, Alignment.CenterVertically)
            ) {
                Text(
                    text = cardName,
                    color = TarotGold,
                    fontWeight = FontWeight.Black,
                    fontSize = 34.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 38.sp
                )

                if (orientation != null) {
                    val badgeColor = if (orientation.equals("Upright", ignoreCase = true)) NeonGreen else NeonBlue
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(badgeColor.copy(alpha = 0.18f))
                            .border(1.dp, badgeColor, RoundedCornerShape(8.dp))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = orientation.uppercase(),
                            color = badgeColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            letterSpacing = 1.sp
                        )
                    }
                }

                val truncatedSummary = if (summaryLine.length > 140) {
                    summaryLine.take(140).trimEnd() + "…"
                } else summaryLine

                Text(
                    text = truncatedSummary,
                    color = TarotTextPrimary,
                    fontSize = 15.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )

                Text(
                    text = "— $displayName",
                    color = TarotTextPrimary.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )

                if (streak > 0) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(TarotGold.copy(alpha = 0.15f))
                            .border(0.5.dp, TarotGold.copy(alpha = 0.6f), RoundedCornerShape(30.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "\uD83D\uDD25 $streak-day streak",
                            color = TarotGold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Get your reading →",
                    color = TarotTextSecondary,
                    fontSize = 12.sp
                )
                Text(
                    text = referralLink.removePrefix("https://"),
                    color = TarotGold,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }
    }
}
