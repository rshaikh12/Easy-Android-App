package com.example.easy.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author Marius Funk, Roxana Shaikh
 */

data class ChatEntry(
    @SerializedName("message") val message: String,
    @SerializedName("userId") val userId: String,
)