package com.example.easy.data.model

import com.google.gson.annotations.SerializedName

/**
 * @author Marius Funk
 */

data class LoginUser(
    @SerializedName("id", alternate=["_id"]) val loginId : String,
    @SerializedName("firstName") val firstName : String,
    @SerializedName("secondName") val secondName : String,
    @SerializedName("email") val email : String,
    @SerializedName("password") val password : String,
    @SerializedName("password2") val password2 : String
)


