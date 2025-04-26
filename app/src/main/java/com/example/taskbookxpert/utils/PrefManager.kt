package com.example.taskbookxpert.utils

import android.content.Context
import android.content.SharedPreferences

object PrefManager {
    private const val PREF_NAME = "settings"
    private const val NOTIF_KEY = "enable_notifications"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun setNotificationEnabled(context: Context, enabled: Boolean) {
        val prefs = getSharedPreferences(context)
        prefs.edit().putBoolean(NOTIF_KEY, enabled).apply()
    }

    fun isNotificationEnabled(context: Context): Boolean {
        val prefs = getSharedPreferences(context)
        return prefs.getBoolean(NOTIF_KEY, true) // Default is true
    }
}