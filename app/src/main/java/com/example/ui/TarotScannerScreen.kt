package com.example.ui

import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

// Live CameraX Tarot card scanner, ported from TaroAiScanner's TarotScannerScreen.
// Adapted to the base app's MainViewModel + ReadingUiState instead of a dedicated TarotViewModel.
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun TarotScannerScreen(
    viewModel: MainViewModel,
    onBack: () -> Unit,
    onNavToReading: () -> Unit
) {
    val readingState by viewModel.readingState.collectAsStateWithLifecycle()

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    var previewView: PreviewView? by remember { mutableStateOf(null) }

    // Reset any stale state from a previous reading when entering the scanner.
    LaunchedEffect(Unit) {
        viewModel.resetReading()
    }

    // Navigate to the reading screen once a result (or error) is available.
    LaunchedEffect(readingState) {
        when (readingState) {
            is ReadingUiState.Success, is ReadingUiState.StructuredSuccess, is ReadingUiState.Error -> onNavToReading()
            else -> Unit
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scan Card", fontWeight = FontWeight.Bold, color = Color(0xFFD4AF37)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = Color(0xFFD4AF37))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF0F081D)
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFF0F081D), Color(0xFF130A24))
                    )
                )
                .padding(padding)
        ) {
            if (cameraPermissionState.status.isGranted) {
                CameraScannerContent(
                    isLoading = readingState is ReadingUiState.Loading,
                    onCapture = {
                        previewView?.bitmap?.let { bitmap ->
                            viewModel.analyzeScannedCardStructured(bitmap)
                        }
                    },
                    onPreviewViewBind = { pv ->
                        previewView = pv
                    }
                )
            } else {
                PermissionOnboardingContent(
                    onRequestPermission = {
                        cameraPermissionState.launchPermissionRequest()
                    }
                )
            }
        }
    }
}

@Composable
private fun CameraScannerContent(
    isLoading: Boolean,
    onCapture: () -> Unit,
    onPreviewViewBind: (PreviewView) -> Unit
) {
    val lifecycleOwner = LocalLifecycleOwner.current

    Box(modifier = Modifier.fillMaxSize()) {
        AndroidView(
            factory = { ctx ->
                PreviewView(ctx).apply {
                    scaleType = PreviewView.ScaleType.FILL_CENTER
                    onPreviewViewBind(this)

                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(surfaceProvider)
                        }
                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview
                            )
                        } catch (exc: Exception) {
                            Log.e("TarotScannerScreen", "Use case binding failed", exc)
                        }
                    }, ContextCompat.getMainExecutor(ctx))
                }
            },
            modifier = Modifier.fillMaxSize()
        )

        // Tarot Card Align Outline Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .aspectRatio(0.6f)
                    .border(2.dp, Color(0xFFD4AF37), RoundedCornerShape(16.dp))
                    .background(Color.Black.copy(alpha = 0.15f))
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = Color(0xFFD4AF37).copy(alpha = 0.4f),
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
        }

        // Top Guidance Text
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(24.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.Black.copy(alpha = 0.6f))
                .padding(12.dp)
        ) {
            Text(
                text = "Align the physical Tarot card within the gold frame and tap capture",
                color = Color.White,
                fontSize = 14.sp,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // Bottom Capture Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(bottom = 48.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.Black.copy(alpha = 0.8f))
                        .padding(24.dp)
                ) {
                    CircularProgressIndicator(color = Color(0xFFD4AF37))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Invoking Gemini Oracle...",
                        color = Color(0xFFD4AF37),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                    Text(
                        text = "Reading the cosmic stars",
                        color = Color.White.copy(alpha = 0.7f),
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            } else {
                Button(
                    onClick = onCapture,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .height(56.dp)
                        .fillMaxWidth(0.8f),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = "Capture",
                        tint = Color.Black
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "REVEAL COSMIC TRUTH",
                        color = Color.Black,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionOnboardingContent(
    onRequestPermission: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
                .background(Color(0xFF241644))
                .border(2.dp, Color(0xFFD4AF37), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = "Camera Access Required",
                tint = Color(0xFFD4AF37),
                modifier = Modifier.size(56.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Mystic Camera Active",
            style = MaterialTheme.typography.headlineMedium.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = (-0.5).sp
            ),
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "To scan physical cards and translate their cosmic energies, the Mystic Lens requires access to your camera.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color(0xFFB1A2C9),
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onRequestPermission,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD4AF37)),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .height(56.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "GRANT CAMERA ACCESS",
                color = Color.Black,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.sp
            )
        }
    }
}
