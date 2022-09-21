package com.example.easy.rest

import com.example.easy.data.model.Item
import com.example.easy.data.model.LoginResponse
import com.example.easy.data.model.LoginUser
import retrofit2.Call
import retrofit2.http.*

/**
 * @Author Marius Funk
 */

interface AccountRestApi {

    /**
     * Create user and return loginUser for login
     */
    @Headers("Content-Type: application/json")
    @POST("/registration")
    fun addUser(@Body userData: LoginUser): Call<LoginUser>

    /**
     * Login user by email/username and password, POST for security
     * Returns LoginResponse with Token and LoggedIn User
     */
    @Headers("Content-Type: application/json")
    @POST("/login")
    fun login(@Query("email") email: String,
                @Query("password") password: String): Call<LoginResponse>


}