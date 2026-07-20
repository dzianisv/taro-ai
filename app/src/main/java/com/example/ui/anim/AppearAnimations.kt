package com.example.ui.anim

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Staggered "deal" entrance: slides content up from [offsetDp] while fading in, delayed by
 * [index] * [staggerMillis] so a row of cards deals in one-by-one.
 */
@Composable
fun DealSlideIn(
    index: Int,
    modifier: Modifier = Modifier,
    staggerMillis: Long = 120L,
    offsetDp: Float = 40f,
    content: @Composable () -> Unit
) {
    val translation = remember { Animatable(offsetDp) }
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        delay(index * staggerMillis)
        launch { translation.animateTo(0f, tween(500, easing = FastOutSlowInEasing)) }
        alpha.animateTo(1f, tween(600))
    }
    Box(
        modifier = modifier.graphicsLayer {
            translationY = translation.value * density
            this.alpha = alpha.value
        }
    ) { content() }
}

/** Fades content in once on first composition, optionally after [delayMillis]. */
@Composable
fun FadeInOnAppear(
    modifier: Modifier = Modifier,
    delayMillis: Int = 0,
    content: @Composable () -> Unit
) {
    val alpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        if (delayMillis > 0) delay(delayMillis.toLong())
        alpha.animateTo(1f, tween(500))
    }
    Box(modifier = modifier.graphicsLayer { this.alpha = alpha.value }) { content() }
}
