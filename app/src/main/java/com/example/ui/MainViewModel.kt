package com.example.ui

import android.app.Activity
import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.AuthManager
import com.example.data.UserProfile
import com.example.data.GeminiRepository
import com.example.data.GeminiResult
import com.example.data.ReadingEntity
import com.example.data.ReadingRepository
import com.example.data.TarotAnalysisResult
import com.example.data.TarotDeck
import com.example.data.TarotReading
import com.example.data.toInterpretationText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Friendly message surfaced when the secure proxy returns HTTP 429 (daily free quota exhausted).
private const val QUOTA_EXCEEDED_MESSAGE = "Daily free reading limit reached — upgrade to Premium."

sealed class ReadingUiState {
    object Idle : ReadingUiState()
    object Loading : ReadingUiState()
    data class Success(val reading: ReadingEntity) : ReadingUiState()
    data class StructuredSuccess(val reading: TarotReading) : ReadingUiState()
    data class Error(val message: String) : ReadingUiState()
}

class MainViewModel(
    private val readingRepository: ReadingRepository,
    private val geminiRepository: GeminiRepository,
    private val getCustomApiKey: () -> String,
    private val getCustomGatewayUrl: () -> String,
    private val getCustomGoogleClientId: () -> String
) : ViewModel() {

    val history: StateFlow<List<ReadingEntity>> = readingRepository.allReadings
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val currentUser: StateFlow<UserProfile?> = AuthManager.currentUserState

    private val _readingState = MutableStateFlow<ReadingUiState>(ReadingUiState.Idle)
    val readingState: StateFlow<ReadingUiState> = _readingState.asStateFlow()

    // Holds the most recently scanned bitmap so the reading screen can display it
    // alongside a StructuredSuccess result. Not persisted (only the reading itself is).
    private val _scannedBitmap = MutableStateFlow<Bitmap?>(null)
    val scannedBitmap: StateFlow<Bitmap?> = _scannedBitmap.asStateFlow()

    private val systemInstruction = """
        You are a warm, direct, and mystical-but-grounded Tarot Master. 
        You interpret the cards presented to you thoughtfully.
        Do not give medical, legal, or financial directives.
        Frame readings for reflection and entertainment.
    """.trimIndent()

    fun resetReading() {
        _readingState.value = ReadingUiState.Idle
        _scannedBitmap.value = null
    }

    fun selectReading(reading: ReadingEntity) {
        _readingState.value = ReadingUiState.Success(reading)
    }

    fun drawDailyCard() {
        viewModelScope.launch {
            _readingState.value = ReadingUiState.Loading
            val cards = TarotDeck.drawRandom(1)
            val prompt = "I drew a single daily card: ${cards.first()}. Please interpret what this means for my day ahead."
            
            when (val result = geminiRepository.generateReading("Daily", cards, prompt, systemInstruction, getCustomApiKey(), getCustomGatewayUrl())) {
                is GeminiResult.QuotaExceeded -> _readingState.value = ReadingUiState.Error(QUOTA_EXCEEDED_MESSAGE)
                is GeminiResult.Success -> {
                    val reading = ReadingEntity(
                        type = "Daily",
                        cardsDrawn = cards.joinToString(", "),
                        interpretation = result.text
                    )
                    readingRepository.insert(reading)
                    _readingState.value = ReadingUiState.Success(reading)
                }
            }
        }
    }

    fun drawThreeCardSpread() {
        viewModelScope.launch {
            _readingState.value = ReadingUiState.Loading
            val cards = TarotDeck.drawRandom(3)
            val prompt = "I drew a 3-card spread (Past, Present, Future): ${cards.joinToString(", ")}. Please provide a grounded, mystical interpretation of this spread."
            
            when (val result = geminiRepository.generateReading("3-Card", cards, prompt, systemInstruction, getCustomApiKey(), getCustomGatewayUrl())) {
                is GeminiResult.QuotaExceeded -> _readingState.value = ReadingUiState.Error(QUOTA_EXCEEDED_MESSAGE)
                is GeminiResult.Success -> {
                    val reading = ReadingEntity(
                        type = "3-Card",
                        cardsDrawn = cards.joinToString(", "),
                        interpretation = result.text
                    )
                    readingRepository.insert(reading)
                    _readingState.value = ReadingUiState.Success(reading)
                }
            }
        }
    }

    fun analyzeScannedCard(imageBase64: String, mimeType: String) {
        viewModelScope.launch {
            _readingState.value = ReadingUiState.Loading
            val prompt = "Please identify the Tarot card shown in this image, and provide a grounded, mystical interpretation/reading of its energy and guidance for the user's day ahead."
            
            when (val result = geminiRepository.analyzeScannedCard(
                imageBase64 = imageBase64,
                mimeType = mimeType,
                prompt = prompt,
                systemInstruction = systemInstruction,
                customApiKey = getCustomApiKey(),
                customGatewayUrl = getCustomGatewayUrl()
            )) {
                is GeminiResult.QuotaExceeded -> _readingState.value = ReadingUiState.Error(QUOTA_EXCEEDED_MESSAGE)
                is GeminiResult.Success -> {
                    val reading = ReadingEntity(
                        type = "Card Scan",
                        cardsDrawn = "Scanned Physical Card",
                        interpretation = result.text
                    )
                    readingRepository.insert(reading)
                    _readingState.value = ReadingUiState.Success(reading)
                }
            }
        }
    }

    // Live camera scan flow: analyzes a captured bitmap and returns a structured TarotReading
    // (cardName/orientation/summary/generalMeaning/advice/warning/luckyElements) instead of free text.
    fun analyzeScannedCardStructured(bitmap: Bitmap, spreadType: String = "Single Card Draw") {
        viewModelScope.launch {
            _scannedBitmap.value = bitmap
            _readingState.value = ReadingUiState.Loading

            when (val result = geminiRepository.analyzeTarotCardStructured(
                bitmap = bitmap,
                promptContext = spreadType,
                customGatewayUrl = getCustomGatewayUrl()
            )) {
                is TarotAnalysisResult.QuotaExceeded -> _readingState.value = ReadingUiState.Error(QUOTA_EXCEEDED_MESSAGE)
                is TarotAnalysisResult.Success -> {
                    // Persist into the existing Room history by serializing the structured fields to text.
                    val reading = ReadingEntity(
                        type = "Card Scan",
                        cardsDrawn = "${result.reading.cardName} (${result.reading.orientation})",
                        interpretation = result.reading.toInterpretationText()
                    )
                    readingRepository.insert(reading)
                    _readingState.value = ReadingUiState.StructuredSuccess(result.reading)
                }
            }
        }
    }

    fun signInWithGoogle(activity: Activity, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val clientId = getCustomGoogleClientId()
            if (clientId.isEmpty()) {
                onError("Google Web Client ID is not configured. Please configure it in Settings.")
                return@launch
            }
            val result = AuthManager.signInWithGoogle(activity, clientId)
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { error -> onError(error.localizedMessage ?: "Google Sign-In failed.") }
            )
        }
    }

    fun signInWithApple(activity: Activity, onSuccess: () -> Unit, onError: (String) -> Unit) {
        viewModelScope.launch {
            val result = AuthManager.signInWithApple(activity)
            result.fold(
                onSuccess = { onSuccess() },
                onFailure = { error -> onError(error.localizedMessage ?: "Apple Sign-In failed.") }
            )
        }
    }

    fun signOut() {
        AuthManager.signOut()
    }

    fun signInOfflineDemo(displayName: String, email: String, providerId: String) {
        AuthManager.signInOfflineDemo(displayName, email, providerId)
    }
}

class MainViewModelFactory(
    private val readingRepository: ReadingRepository,
    private val geminiRepository: GeminiRepository,
    private val getCustomApiKey: () -> String,
    private val getCustomGatewayUrl: () -> String,
    private val getCustomGoogleClientId: () -> String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(readingRepository, geminiRepository, getCustomApiKey, getCustomGatewayUrl, getCustomGoogleClientId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
