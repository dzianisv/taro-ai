package com.example.data

import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.CustomCredential
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.OAuthProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

data class UserProfile(
    val uid: String,
    val displayName: String?,
    val email: String?,
    val photoUrl: String?,
    val providerId: String
)

object AuthManager {
    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    
    private val _currentUserState = MutableStateFlow<UserProfile?>(null)
    val currentUserState: StateFlow<UserProfile?> = _currentUserState

    init {
        // Observe Firebase Auth changes and map to our UserProfile model
        auth.addAuthStateListener { firebaseAuth ->
            val user = firebaseAuth.currentUser
            if (user != null) {
                _currentUserState.value = UserProfile(
                    uid = user.uid,
                    displayName = user.displayName ?: user.email?.substringBefore("@"),
                    email = user.email,
                    photoUrl = user.photoUrl?.toString(),
                    providerId = user.providerId
                )
            } else {
                _currentUserState.value = null
            }
        }
    }

    /**
     * Triggers Google Sign-In using Android Credential Manager
     */
    suspend fun signInWithGoogle(activity: Activity, webClientId: String): Result<UserProfile> = withContext(Dispatchers.IO) {
        try {
            val credentialManager = CredentialManager.create(activity)

            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(webClientId)
                .setAutoSelectEnabled(false)
                .build()

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            val result = credentialManager.getCredential(activity, request)
            val credential = result.credential

            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken

                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = auth.signInWithCredential(firebaseCredential).await()
                val user = authResult.user

                if (user != null) {
                    val profile = UserProfile(
                        uid = user.uid,
                        displayName = user.displayName ?: user.email?.substringBefore("@"),
                        email = user.email,
                        photoUrl = user.photoUrl?.toString(),
                        providerId = "google.com"
                    )
                    Result.success(profile)
                } else {
                    Result.failure(Exception("Failed to get authenticated user from Firebase."))
                }
            } else {
                Result.failure(Exception("Unexpected credential type: ${credential.type}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Triggers Apple Sign-In using Firebase OAuthProvider (Web-based callback)
     */
    suspend fun signInWithApple(activity: Activity): Result<UserProfile> = withContext(Dispatchers.Main) {
        try {
            val provider = OAuthProvider.newBuilder("apple.com")
            provider.scopes = listOf("email", "name")
            
            // Starts Web-based Apple sign-in inside a Chrome Custom Tab
            val authResult = auth.startActivityForSignInWithProvider(activity, provider.build()).await()
            val user = authResult.user

            if (user != null) {
                val profile = UserProfile(
                    uid = user.uid,
                    displayName = user.displayName ?: user.email?.substringBefore("@"),
                    email = user.email,
                    photoUrl = user.photoUrl?.toString(),
                    providerId = "apple.com"
                )
                Result.success(profile)
            } else {
                Result.failure(Exception("Failed to authenticate with Apple via Firebase."))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Log out from Firebase Auth
     */
    fun signOut() {
        auth.signOut()
        _currentUserState.value = null
    }

    /**
     * Signs in with a local custom profile for developers / testing offline
     */
    fun signInOfflineDemo(displayName: String, email: String, providerId: String) {
        _currentUserState.value = UserProfile(
            uid = "demo_uid_${System.currentTimeMillis()}",
            displayName = displayName,
            email = email,
            photoUrl = null,
            providerId = providerId
        )
    }
}
