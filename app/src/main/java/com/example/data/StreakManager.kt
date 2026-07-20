package com.example.data

import android.content.Context

// Tracks a simple daily-reading streak using SharedPreferences (no Room table needed).
// minSdk=24 with no core library desugoring enabled, so java.time is unavailable here;
// instead we use "epoch day" math: System.currentTimeMillis() / 86_400_000L gives a
// UTC day index that increments exactly once every 24h, which is enough to detect
// "same day", "consecutive day", and "gap" without any calendar/timezone APIs.
object StreakManager {
    private const val PREFS_NAME = "taro_streak"
    private const val KEY_LAST_DAY = "last_reading_day"
    private const val KEY_STREAK = "streak_count"

    private const val MILLIS_PER_DAY = 86_400_000L

    private fun todayEpochDay(): Long = System.currentTimeMillis() / MILLIS_PER_DAY

    /**
     * Call once per completed reading. Returns the (possibly updated) streak count.
     * - Same day as last recorded reading: streak unchanged (already counted today).
     * - Exactly one day after the last recorded reading: streak += 1.
     * - Any bigger gap (or first ever reading): streak resets to 1.
     */
    fun recordReadingToday(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val today = todayEpochDay()
        val lastDay = prefs.getLong(KEY_LAST_DAY, -1L)
        val currentStreak = prefs.getInt(KEY_STREAK, 0)

        val newStreak = when {
            lastDay == today -> currentStreak
            lastDay == today - 1 -> currentStreak + 1
            else -> 1
        }

        prefs.edit()
            .putLong(KEY_LAST_DAY, today)
            .putInt(KEY_STREAK, newStreak)
            .apply()

        return newStreak
    }

    /**
     * Returns the streak count for display purposes. If the last recorded reading was
     * more than one day ago, the streak is stale/broken so we display 0 (without
     * mutating storage — recordReadingToday() is the only writer).
     */
    fun getCurrentStreak(context: Context): Int {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val today = todayEpochDay()
        val lastDay = prefs.getLong(KEY_LAST_DAY, -1L)
        val streak = prefs.getInt(KEY_STREAK, 0)
        return if (lastDay < today - 1) 0 else streak
    }
}
