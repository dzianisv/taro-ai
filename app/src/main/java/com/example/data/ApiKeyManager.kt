package com.example.data

import android.content.Context

object ApiKeyManager {
    private const val PREFS_NAME = "taro_settings"
    private const val KEY_API_KEY = "gemini_api_key"
    private const val KEY_GATEWAY_URL = "gemini_gateway_url"
    private const val KEY_GOOGLE_CLIENT_ID = "google_web_client_id"

    fun saveApiKey(context: Context, key: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_API_KEY, key.trim()).apply()
    }

    fun getApiKey(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_API_KEY, "") ?: ""
    }

    fun saveGatewayUrl(context: Context, url: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_GATEWAY_URL, url.trim()).apply()
    }

    fun getGatewayUrl(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_GATEWAY_URL, "") ?: ""
    }

    fun saveGoogleClientId(context: Context, clientId: String) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_GOOGLE_CLIENT_ID, clientId.trim()).apply()
    }

    fun getGoogleClientId(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_GOOGLE_CLIENT_ID, "") ?: ""
    }
}
