package com.example.easy.rest

import android.media.Image
import com.example.easy.data.model.Item
import com.example.easy.data.model.LoggedInUser
import com.example.easy.data.model.LoginResponse
import com.example.easy.data.model.LoginUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @Author Marius Funk
 *
 * Service for the AccountRestApi
 *
 * * Mostly boilerplate therefore uncommented
 */

class AccountRestApiService{

    private val retrofit = ServiceBuilder.buildService(AccountRestApi::class.java)

    fun addUser(userData: LoginUser): LoginUser? {
        return retrofit.addUser(userData).execute().body()
    }


    fun login(email: String, password: String): LoginResponse? {
        return retrofit.login(email, password).execute().body()
    }
}