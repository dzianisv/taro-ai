package com.example.ui.share

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

// Parameters needed to render + share a single ShareableReadingCard capture.
internal data class ShareParams(
    val cardName: String,
    val orientation: String?,
    val summaryLine: String,
    val displayName: String,
    val streak: Int,
    val referralLink: String,
)

/**
 * Controller returned by [rememberShareCapture]. Calling [share] triggers an offscreen
 * composition + capture of [ShareableReadingCard], then launches the Android share sheet
 * with the resulting PNG.
 */
class ShareCaptureController internal constructor(
    private val paramsState: androidx.compose.runtime.MutableState<ShareParams?>,
) {
    fun share(
        cardName: String,
        orientation: String?,
        summaryLine: String,
        displayName: String,
        streak: Int,
        referralLink: String,
    ) {
        paramsState.value = ShareParams(cardName, orientation, summaryLine, displayName, streak, referralLink)
    }
}

/**
 * Provides a share controller AND emits the offscreen capture host into the composition
 * (only while a share is in flight). Call this once near the top of a screen-level
 * composable; it renders nothing visible in the normal layout.
 */
@Composable
fun rememberShareCapture(): ShareCaptureController {
    val context = LocalContext.current
    val paramsState = remember { mutableStateOf<ShareParams?>(null) }
    val graphicsLayer = rememberGraphicsLayer()

    val currentParams = paramsState.value
    if (currentParams != null) {
        // Offscreen host: laid out & drawn (so the GraphicsLayer records real pixels) but
        // positioned far outside the visible viewport so nothing appears on top of the UI.
        Box(modifier = Modifier.wrapContentSize(unbounded = true)) {
            Box(
                modifier = Modifier
                    .offset(x = 6000.dp)
                    .drawWithContent {
                        graphicsLayer.record { this@drawWithContent.drawContent() }
                        drawLayer(graphicsLayer)
                    }
            ) {
                ShareableReadingCard(
                    cardName = currentParams.cardName,
                    orientation = currentParams.orientation,
                    summaryLine = currentParams.summaryLine,
                    displayName = currentParams.displayName,
                    streak = currentParams.streak,
                    referralLink = currentParams.referralLink,
                )
            }
        }

        LaunchedEffect(currentParams) {
            // The layer is only populated with pixels after a real layout+draw pass runs,
            // so wait for it to report a non-zero size before capturing (guards against a
            // blank/1x1 bitmap if we captured on the very first composition frame).
            var attempts = 0
            while (graphicsLayer.size.width <= 0 && attempts < 20) {
                withFrameNanos {}
                attempts++
            }
            val bitmap = runCatching { graphicsLayer.toImageBitmap().asAndroidBitmap() }.getOrNull()
            paramsState.value = null // stop composing the offscreen host
            if (bitmap != null) {
                withContext(Dispatchers.IO) {
                    saveAndShareBitmap(context, bitmap, currentParams)
                }
            }
        }
    }

    return remember { ShareCaptureController(paramsState) }
}

private fun saveAndShareBitmap(context: Context, bitmap: Bitmap, params: ShareParams) {
    val dir = File(context.cacheDir, "shared_images").apply { mkdirs() }
    val file = File(dir, "taro_share_${System.currentTimeMillis()}.png")
    FileOutputStream(file).use { out ->
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
    }

    val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    val caption = "My Taro reading \uD83D\uDD2E ${params.cardName} — ${params.summaryLine.take(80)}. " +
        "Get yours → ${params.referralLink}"

    val sendIntent = Intent(Intent.ACTION_SEND).apply {
        type = "image/*"
        putExtra(Intent.EXTRA_STREAM, uri)
        putExtra(Intent.EXTRA_TEXT, caption)
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    val chooser = Intent.createChooser(sendIntent, "Share your reading").apply {
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    }
    context.startActivity(chooser)
}
