package com.example.easy.rest

import com.example.easy.data.model.Chat
import com.example.easy.data.model.ChatEntry
import com.example.easy.data.model.Item
import retrofit2.Call
import retrofit2.http.*

/**
 * @Author Marius Funk
 */

interface ChatRestApi {

    /**
     * Posts new chat and returns entry
     */
    @Headers("Content-Type: application/json")
    @POST("/chat")
    fun addChat(@Header("Authorization") token: String,@Body chatData: Chat): Call<Chat>


    /**
     * Adds chat message and returns message
     */
    @Headers("Content-Type: application/json")
    @POST("/chat/message")
    fun addChatMessage(@Body chatEntryData: ChatEntry, @Query("userId1") userId1: String, @Query("userId2") userId2 : String): Call<ChatEntry>

    /**
     * get chat between two users
     */
    @Headers("Content-Type: application/json")
    @GET("/chat")
    fun getChat(@Header("Authorization") token: String,@Query("userId1") userId1: String,
                @Query("userId2") userId2: String): Call<Chat>

    /**
     * get all chats of a user
     */
    @Headers("Content-Type: application/json")
    @GET("/chat/byUserId")
    fun getChatsByUserId(@Query("userId") userId: String): Call<ArrayList<Chat>>

}