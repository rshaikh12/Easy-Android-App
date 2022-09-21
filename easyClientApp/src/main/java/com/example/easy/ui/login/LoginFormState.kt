package com.example.easy.ui.login

/**
 * Boilerplate code, rework by
 * @author Marius Funk
 *
 * Data validation state of the login form.
 */
data class LoginFormState(val usernameError: Int? = null,
                          val passwordError: Int? = null,
                          val password2Error: Int? = null,
                          val firstNameError: Int? = null,
                          val secondNameError: Int? = null,
                          val isDataValid: Boolean = false,
                          val isRegisterValid: Boolean = false)