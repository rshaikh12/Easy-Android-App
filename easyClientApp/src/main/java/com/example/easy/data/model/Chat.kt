package com.example.easy.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author Marius Funk
 */

data class Chat(
    @SerializedName("userId1") val userId1: String,
    @SerializedName("userId2") val userId2: String,
    @SerializedName("chat") val chatEntries: ArrayList<ChatEntry>
)