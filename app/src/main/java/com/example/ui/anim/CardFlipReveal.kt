package com.example.ui.anim

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp

/**
 * One-shot 3D card reveal: rotates from the card back (rotationY 180°) to the face (0°) when the
 * composable first enters composition. The rendered face is swapped at the 90° midpoint, and the
 * back is counter-rotated so it never appears mirrored. Callers must give [back] and [front] the
 * same size for a clean flip.
 */
@Composable
fun CardFlipReveal(
    modifier: Modifier = Modifier,
    durationMillis: Int = 700,
    back: @Composable () -> Unit = { CosmicCardBack() },
    front: @Composable () -> Unit
) {
    val rotation = remember { Animatable(180f) }
    LaunchedEffect(Unit) {
        rotation.animateTo(0f, animationSpec = tween(durationMillis, easing = FastOutSlowInEasing))
    }
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        val angle = rotation.value
        if (angle > 90f) {
            Box(
                modifier = Modifier.graphicsLayer {
                    rotationY = angle - 180f
                    cameraDistance = 14f * density
                }
            ) { back() }
        } else {
            Box(
                modifier = Modifier.graphicsLayer {
                    rotationY = angle
                    cameraDistance = 14f * density
                }
            ) { front() }
        }
    }
}

/** Gold-bordered mystical card back showing a central sigil; the default [CardFlipReveal] back. */
@Composable
fun CosmicCardBack(modifier: Modifier = Modifier.width(160.dp).height(240.dp)) {
    val gold = MaterialTheme.colorScheme.primary
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Brush.verticalGradient(listOf(Color(0xFF241B4A), Color(0xFF12121A))))
            .border(1.5.dp, gold.copy(alpha = 0.7f), RoundedCornerShape(12.dp)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = gold,
            modifier = Modifier.width(56.dp).height(56.dp)
        )
    }
}
