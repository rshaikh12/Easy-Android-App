package com.example.easy.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author Marius Funk
 *
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class LoggedInUser(
    @SerializedName("id", alternate=["_id"]) val userId: String,
    @SerializedName("displayName") val displayName: String,
    @SerializedName("firstName") val firstName: String,
    @SerializedName("secondName") val secondName: String,
    @SerializedName("email") val email: String,
    @SerializedName("loginId") val loginId: String,
    @SerializedName("longditude") var longditude: Double,
    @SerializedName("latitude") var latitude: Double,

    )