package com.example

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.ApiKeyManager
import com.example.data.AppDatabase
import com.example.data.GeminiRepository
import com.example.data.ReadingRepository
import com.example.data.ReferralManager
import com.example.ui.HomeScreen
import com.example.ui.MainViewModel
import com.example.ui.MainViewModelFactory
import com.example.ui.ReadingScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()

    // Safely initialize Firebase App
    try {
        com.google.firebase.FirebaseApp.getInstance()
    } catch (e: IllegalStateException) {
        val options = com.google.firebase.FirebaseOptions.Builder()
            .setApiKey("AIzaSyFakeKey_Placeholder_For_Compilation")
            .setApplicationId("1:123456789:android:abcdef")
            .setProjectId("fake-project-id")
            .setDatabaseUrl("https://fake-project-id.firebaseio.com")
            .setGcmSenderId("123456789")
            .build()
        com.google.firebase.FirebaseApp.initializeApp(applicationContext, options)
    }
    
    val database = AppDatabase.getDatabase(this)
    val readingRepository = ReadingRepository(database.readingDao())
    val geminiRepository = GeminiRepository()
    
    setContent {
      MyApplicationTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            TaroApp(
                readingRepository, 
                geminiRepository, 
                getCustomApiKey = { ApiKeyManager.getApiKey(this) },
                getCustomGatewayUrl = { ApiKeyManager.getGatewayUrl(this) },
                getCustomGoogleClientId = { ApiKeyManager.getGoogleClientId(this) }
            )
        }
      }
    }

    handleDeepLink(intent)
  }

  override fun onNewIntent(intent: Intent) {
    super.onNewIntent(intent)
    setIntent(intent)
    handleDeepLink(intent)
  }

  // Captures a referral code from either https://taro.app/r/<code> or taro://r/<code>
  // deep links and stashes it via ReferralManager. Defensive/no-op on any normal
  // launcher intent (data == null) or malformed URI — must never crash app launch.
  private fun handleDeepLink(intent: Intent?) {
    try {
        val uri = intent?.data ?: return
        val code = when (uri.scheme) {
            "https" -> uri.lastPathSegment
            "taro" -> uri.lastPathSegment ?: uri.pathSegments.firstOrNull()
            else -> null
        }
        if (!code.isNullOrBlank()) {
            ReferralManager.saveIncomingReferralCode(this, code)
        }
    } catch (e: Exception) {
        Log.w("MainActivity", "Failed to parse deep link intent", e)
    }
  }
}

@Composable
fun TaroApp(
    readingRepository: ReadingRepository,
    geminiRepository: GeminiRepository,
    getCustomApiKey: () -> String,
    getCustomGatewayUrl: () -> String,
    getCustomGoogleClientId: () -> String
) {
    val navController = rememberNavController()
    val appContext = LocalContext.current.applicationContext
    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(
            readingRepository,
            geminiRepository,
            getCustomApiKey,
            getCustomGatewayUrl,
            getCustomGoogleClientId,
            appContext = appContext
        )
    )

    NavHost(navController = navController, startDestination = "home") {
        // Shared fade + horizontal slide transitions for smooth navigation between screens.
        val slideDuration = 400
        val fadeDuration = 300
        composable(
            route = "home",
            enterTransition = { fadeIn(tween(fadeDuration)) + slideInHorizontally(tween(slideDuration)) { it / 4 } },
            exitTransition = { fadeOut(tween(fadeDuration)) + slideOutHorizontally(tween(slideDuration)) { -it / 4 } },
            popEnterTransition = { fadeIn(tween(fadeDuration)) + slideInHorizontally(tween(slideDuration)) { -it / 4 } },
            popExitTransition = { fadeOut(tween(fadeDuration)) + slideOutHorizontally(tween(slideDuration)) { it / 4 } }
        ) {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToReading = {
                    navController.navigate("reading")
                },
                onNavigateToScanner = {
                    navController.navigate("scanner")
                }
            )
        }
        composable(
            route = "scanner",
            enterTransition = { fadeIn(tween(fadeDuration)) + slideInHorizontally(tween(slideDuration)) { it / 4 } },
            exitTransition = { fadeOut(tween(fadeDuration)) + slideOutHorizontally(tween(slideDuration)) { -it / 4 } },
            popEnterTransition = { fadeIn(tween(fadeDuration)) + slideInHorizontally(tween(slideDuration)) { -it / 4 } },
            popExitTransition = { fadeOut(tween(fadeDuration)) + slideOutHorizontally(tween(slideDuration)) { it / 4 } }
        ) {
            com.example.ui.TarotScannerScreen(
                viewModel = viewModel,
                onBack = {
                    navController.popBackStack()
                },
                onNavToReading = {
                    navController.navigate("reading") {
                        popUpTo("scanner") { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "reading",
            enterTransition = { fadeIn(tween(fadeDuration)) + slideInHorizontally(tween(slideDuration)) { it / 4 } },
            exitTransition = { fadeOut(tween(fadeDuration)) + slideOutHorizontally(tween(slideDuration)) { -it / 4 } },
            popEnterTransition = { fadeIn(tween(fadeDuration)) + slideInHorizontally(tween(slideDuration)) { -it / 4 } },
            popExitTransition = { fadeOut(tween(fadeDuration)) + slideOutHorizontally(tween(slideDuration)) { it / 4 } }
        ) {
            ReadingScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
