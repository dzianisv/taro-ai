package com.example.ui.anim

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

private data class Star(
    val x: Float,
    val y: Float,
    val radius: Float,
    val phase: Float,
    val baseAlpha: Float
)

/**
 * A subtle animated overlay layer: a slow-drifting radial glow plus a fixed starfield that
 * twinkles out of sync. Designed to sit ON TOP of the existing background image + dark gradient
 * (it draws only low-alpha content over transparency, so the photo stays visible underneath).
 */
@Composable
fun CosmicBackground(
    modifier: Modifier = Modifier,
    starCount: Int = 55,
    starTint: Color = Color(0xFFFFD700),
    glowTint: Color = Color(0xFF6B4EE6)
) {
    // Generated once with a stable seed so positions/sizes stay fixed across recompositions;
    // each star gets its own phase so the field doesn't pulse in unison.
    val stars = remember(starCount) {
        val rnd = Random(4242)
        List(starCount) {
            Star(
                x = rnd.nextFloat(),
                y = rnd.nextFloat(),
                radius = 0.6f + rnd.nextFloat() * 1.6f,
                phase = rnd.nextFloat() * (2f * PI.toFloat()),
                baseAlpha = 0.25f + rnd.nextFloat() * 0.55f
            )
        }
    }

    val transition = rememberInfiniteTransition(label = "cosmic")
    // 18s drift keeps the glow motion calm and battery-light.
    val drift by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(18000, easing = LinearEasing)),
        label = "drift"
    )
    // 6s shared twinkle clock; per-star phase offset staggers the pulse.
    val twinkle by transition.animateFloat(
        initialValue = 0f,
        targetValue = 2f * PI.toFloat(),
        animationSpec = infiniteRepeatable(tween(6000, easing = LinearEasing)),
        label = "twinkle"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val angle = drift * 2f * PI.toFloat()
        val cx = size.width * (0.5f + 0.28f * cos(angle))
        val cy = size.height * (0.42f + 0.22f * sin(angle))
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(glowTint.copy(alpha = 0.16f), Color.Transparent),
                center = Offset(cx, cy),
                radius = size.maxDimension * 0.7f
            )
        )
        stars.forEach { star ->
            val a = (0.35f + 0.65f * (0.5f + 0.5f * sin(twinkle + star.phase))) * star.baseAlpha
            drawCircle(
                color = starTint.copy(alpha = a.coerceIn(0f, 1f)),
                radius = star.radius * density,
                center = Offset(star.x * size.width, star.y * size.height)
            )
        }
    }
}
