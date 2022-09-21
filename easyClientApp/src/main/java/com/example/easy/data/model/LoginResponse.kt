package com.example.easy.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author Marius Funk
 *
 * Return object for the login, contains token and user
 */


data class LoginResponse (
    @SerializedName("token") val token: String,
    @SerializedName("loggedInUser", alternate=["userData"]) val loggedInUser: LoggedInUser
)