package com.example.easy.rest

import com.example.easy.data.model.Item
import com.example.easy.data.model.LoggedInUser
import retrofit2.Call
import retrofit2.http.*
import java.util.*

/**
 * @Author Marius Funk
 */

interface ProfileRestApi {
    /**
     * UNUSED
     * Would add a new profile but this is done in the backend immediately
     */
    @Headers("Content-Type: application/json")
    @POST("/profile")
    fun addProfile(@Header("Authorization") token: String, @Body profileData: LoggedInUser): Call<LoggedInUser>


    /**
     * Returns LoggedInUser by id
     */
    @Headers("Content-Type: application/json")
    @GET("/profile")
    fun getProfile(@Query("userId") userId: String): Call<LoggedInUser>

    /**
     * Returns all LoggenInUser
     * Used for the map
     */
    @Headers("Content-Type: application/json")
    @GET("/profile/all")
    fun getAllProfiles(): Call<ArrayList<LoggedInUser>>

    /**
     * Returns the loggend in user when logging in
     */
    @Headers("Content-Type: application/json")
    @GET("/profile/byLoginId")
    fun getProfileByLoginId(@Query("loginId") loginId: String): Call<LoggedInUser>

    /**
     * Update the logged in users profile (mostly location)
     */
    @Headers("Content-Type: application/json")
    @PUT("/profile")
    fun changeProfile(@Header("Authorization") token: String, @Body profileData: LoggedInUser): Call<LoggedInUser>
}