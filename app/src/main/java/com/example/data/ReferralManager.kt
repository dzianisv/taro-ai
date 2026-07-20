package com.example.data

import android.content.Context
import java.util.UUID

// Generates and persists a per-install referral code so the share loop works fully
// offline / without sign-in. Following ApiKeyManager's SharedPreferences-based style.
object ReferralManager {
    private const val PREFS_NAME = "taro_referral"
    private const val KEY_OWN_CODE = "own_referral_code"
    private const val KEY_INCOMING = "incoming_referral_code"

    private const val REFERRAL_HOST = "https://taro.app/r/"

    fun getOrCreateReferralCode(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val existing = prefs.getString(KEY_OWN_CODE, null)
        if (!existing.isNullOrBlank()) return existing

        val generated = UUID.randomUUID().toString().replace("-", "").take(8).uppercase()
        prefs.edit().putString(KEY_OWN_CODE, generated).apply()
        return generated
    }

    fun getReferralLink(context: Context): String {
        return REFERRAL_HOST + getOrCreateReferralCode(context)
    }

    fun saveIncomingReferralCode(context: Context, code: String) {
        if (code.isBlank()) return
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_INCOMING, code).apply()
    }

    fun getIncomingReferralCode(context: Context): String? {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getString(KEY_INCOMING, null)
    }
}
