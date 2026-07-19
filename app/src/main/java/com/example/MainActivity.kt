package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.data.ApiKeyManager
import com.example.data.AppDatabase
import com.example.data.GeminiRepository
import com.example.data.ReadingRepository
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
    val viewModel: MainViewModel = viewModel(
        factory = MainViewModelFactory(readingRepository, geminiRepository, getCustomApiKey, getCustomGatewayUrl, getCustomGoogleClientId)
    )

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onNavigateToReading = {
                    navController.navigate("reading")
                }
            )
        }
        composable("reading") {
            ReadingScreen(
                viewModel = viewModel,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
