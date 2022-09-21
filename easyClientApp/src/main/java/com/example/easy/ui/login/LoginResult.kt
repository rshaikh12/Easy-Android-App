package com.example.easy.ui.login

/**
 * Authentication result : success (user details) or error message.
 *
 * Unused
 */
data class LoginResult(
        val success: Boolean = false,
        val error: Int? = null
)