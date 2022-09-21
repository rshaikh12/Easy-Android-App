package com.example.easy.rest

import android.media.Image
import com.example.easy.data.model.Chat
import com.example.easy.data.model.Item
import com.example.easy.data.model.LoggedInUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @Author Marius Funk
 *
 * Mostly boilerplate therefore uncommented
 */

class ProfileRestApiService{

    private val retrofit = ServiceBuilder.buildService(ProfileRestApi::class.java)


    fun addProfile(token: String, profileData: LoggedInUser, onResult: (LoggedInUser?) -> Unit){
        retrofit.addProfile(token,profileData).enqueue(
            object : Callback<LoggedInUser> {
                override fun onFailure(call: Call<LoggedInUser>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<LoggedInUser>, response: Response<LoggedInUser>) {
                    val profileResponseData = response.body()
                    onResult(profileResponseData)
                }
            }
        )
    }

    fun changeProfile(token: String,profileData: LoggedInUser, onResult: (LoggedInUser?) -> Unit){
        retrofit.changeProfile(token, profileData).enqueue(
            object : Callback<LoggedInUser> {
                override fun onFailure(call: Call<LoggedInUser>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<LoggedInUser>, response: Response<LoggedInUser>) {
                    val changedProfile = response.body()
                    onResult(changedProfile)
                }
            }
        )
    }

    fun getProfile(id: String, onResult: (LoggedInUser?) -> Unit){
        retrofit.getProfile(id).enqueue(
            object : Callback<LoggedInUser> {
                override fun onFailure(call: Call<LoggedInUser>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<LoggedInUser>, response: Response<LoggedInUser>) {
                    val profileData = response.body()
                    onResult(profileData)
                }
            }
        )
    }

    fun getAllProfiles(onResult: (ArrayList<LoggedInUser>?) -> Unit){
        retrofit.getAllProfiles().enqueue(
            object : Callback<ArrayList<LoggedInUser>> {
                override fun onFailure(call: Call<ArrayList<LoggedInUser>>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<ArrayList<LoggedInUser>>, response: Response<ArrayList<LoggedInUser>>) {
                    val profilesData = response.body()
                    onResult(profilesData)
                }
            }
        )
    }

    fun getProfileByLoginId(loginId: String): LoggedInUser? {
        return retrofit.getProfileByLoginId(loginId).execute().body()
    }
}