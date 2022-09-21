package com.example.easy.rest

import android.content.Context
import android.content.SharedPreferences
import com.example.easy.R

/**
 * @Author Marius Funk
 *
 * Manages the JWT and therefore the session
 *
 */
class SessionManager (context: Context) {
    private var prefs: SharedPreferences = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
    //const not possible in classes
    companion object {
        const val USER_TOKEN = "user_token"
    }

    /**
     * Save the JWT in the preferences
     */
    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    /**
     * Get the JWT from the preferences
     */
    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }
}