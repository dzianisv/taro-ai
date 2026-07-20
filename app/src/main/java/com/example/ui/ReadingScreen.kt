package com.example.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.AuthManager
import com.example.data.ReadingEntity
import com.example.data.ReferralManager
import com.example.data.StreakManager
import com.example.ui.anim.CardFlipReveal
import com.example.ui.anim.CosmicBackground
import com.example.ui.anim.CosmicCardBack
import com.example.ui.anim.DealSlideIn
import com.example.ui.anim.FadeInOnAppear
import com.example.ui.anim.MysticalLoader
import com.example.ui.share.rememberShareCapture

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReadingScreen(
    viewModel: MainViewModel,
    onNavigateBack: () -> Unit
) {
    val readingState by viewModel.readingState.collectAsStateWithLifecycle()
    val scannedBitmap by viewModel.scannedBitmap.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Your Reading") },
                navigationIcon = {
                    IconButton(onClick = { 
                        viewModel.resetReading()
                        onNavigateBack() 
                    }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            // Background Image
            Image(
                painter = painterResource(id = R.drawable.img_mystical_bg_1784494161352), // Adjust name based on actual generation
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            
            // Dark Overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.5f),
                                Color.Black.copy(alpha = 0.8f),
                                Color.Black.copy(alpha = 1.0f)
                            )
                        )
                    )
            )

            // Animated cosmic overlay (drifting glow + twinkling starfield) atop the static art.
            CosmicBackground(modifier = Modifier.fillMaxSize())

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (val state = readingState) {
                    is ReadingUiState.Idle -> {
                        // Shouldn't really happen if we show loading right away
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                    is ReadingUiState.Loading -> {
                        MysticalLoader()
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Consulting the spirits...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    is ReadingUiState.Success -> {
                        ReadingContent(reading = state.reading)
                    }
                    is ReadingUiState.StructuredSuccess -> {
                        StructuredReadingContent(reading = state.reading, bitmap = scannedBitmap)
                    }
                    is ReadingUiState.Error -> {
                        Text(
                            text = state.message,
                            color = MaterialTheme.colorScheme.error,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ReadingContent(reading: ReadingEntity) {
    val context = LocalContext.current
    val shareCapture = rememberShareCapture()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (reading.type == "3-Card") {
            // Past / Present / Future dealt in with a staggered slide + fade.
            ThreeCardSpread(cardsDrawn = reading.cardsDrawn)
        } else {
            // Single-card layout with a one-shot 3D flip reveal (back -> face).
            CardFlipReveal(
                back = { CosmicCardBack() },
                front = {
                    if (reading.type == "Card Scan") {
                        Box(
                            modifier = Modifier
                                .width(160.dp)
                                .height(240.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = "Scanned Card",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(48.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "PHYSICAL SCAN",
                                    style = MaterialTheme.typography.labelMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    } else {
                        Image(
                            painter = painterResource(id = R.drawable.img_app_icon_1784494151841),
                            contentDescription = "Tarot Card",
                            modifier = Modifier
                                .width(160.dp)
                                .height(240.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Cards Drawn:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = reading.cardsDrawn,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interpretation eases in after the reveal so text doesn't pop abruptly.
        FadeInOnAppear(delayMillis = 250) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = reading.interpretation,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val displayName = AuthManager.currentUserState.value?.displayName ?: "Seeker"
                val streak = StreakManager.getCurrentStreak(context)
                val referralLink = ReferralManager.getReferralLink(context)
                val firstCard = reading.cardsDrawn.substringBefore(",").trim()
                shareCapture.share(
                    cardName = firstCard.ifBlank { reading.cardsDrawn },
                    orientation = null,
                    summaryLine = deriveShareSummary(reading.interpretation),
                    displayName = displayName,
                    streak = streak,
                    referralLink = referralLink
                )
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(imageVector = Icons.Default.Share, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Share this reading")
        }
    }
}

// The offline fallback (LocalTarotInterpreter) opens every free-text reading with a fixed
// "Ethereal Connection Mode" disclaimer paragraph. That's useful in-app context but looks
// broken on a viral share card, so skip past it (and strip markdown/heading noise) to find
// the first substantive line of actual card guidance.
private fun deriveShareSummary(interpretation: String): String {
    val paragraphs = interpretation.split("\n\n")
    // Known fixed shape: [0] "🔮 *Ethereal Connection Mode...*", [1] disclaimer sentence,
    // [2] "*Daily Card: X*" / "*Three-Card Spread Reading*", [3+] actual card guidance.
    val body = if (paragraphs.firstOrNull()?.contains("Ethereal Connection Mode") == true) {
        paragraphs.drop(2).joinToString("\n\n")
    } else {
        interpretation
    }
    val bestLine = body
        .replace(Regex("[*_#]"), "")
        .lineSequence()
        .map { it.trim().replace(Regex("^[^\\p{L}\\p{N}]+"), "") }
        .firstOrNull { line ->
            line.length > 20 && !line.endsWith(":") &&
                !line.startsWith("Daily Card") && !line.startsWith("Three-Card") &&
                !line.contains("cosmic currents are swirling") && !line.contains("Ethereal Connection")
        }
        ?: body.replace(Regex("[*_#]"), "").trim()
    return if (bestLine.length > 130) bestLine.take(130).trimEnd() + "…" else bestLine
}

@Composable
private fun ThreeCardSpread(cardsDrawn: String) {
    val labels = listOf("PAST", "PRESENT", "FUTURE")
    val cards = cardsDrawn.split(",").map { it.trim() }
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        labels.forEachIndexed { index, label ->
            val cardName = cards.getOrNull(index) ?: "—"
            DealSlideIn(index = index, modifier = Modifier.weight(1f)) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = label,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.7f))
                            .border(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = cardName,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}
