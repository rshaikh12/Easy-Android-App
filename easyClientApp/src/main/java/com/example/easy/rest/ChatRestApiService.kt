package com.example.easy.rest

import com.example.easy.data.model.Chat
import com.example.easy.data.model.ChatEntry
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * @Author Marius Funk
 *
 * Service for the ChatRestApi
 *
 * Mostly boilerplate therefore uncommented
 */

class ChatRestApiService{

    private val retrofit = ServiceBuilder.buildService(ChatRestApi::class.java)

    fun addChat(token: String,chatData: Chat, onResult: (Chat?) -> Unit){
        retrofit.addChat(token,chatData).enqueue(
            object : Callback<Chat> {
                override fun onFailure(call: Call<Chat>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<Chat>, response: Response<Chat>) {
                    val chatResponseData = response.body()
                    onResult(chatResponseData)
                }
            }
        )
    }

    fun addChatMessage(chatEntryData: ChatEntry, chatData: Chat, onResult: (ChatEntry?) -> Unit){
        retrofit.addChatMessage(chatEntryData, chatData.userId1, chatData.userId2).enqueue(
            object : Callback<ChatEntry> {
                override fun onFailure(call: Call<ChatEntry>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<ChatEntry>, response: Response<ChatEntry>) {
                    val chatData = response.body()
                    onResult(chatData)
                }
            }
        )
    }

    fun getChat(token: String,userId1: String, userId2: String, onResult: (Chat?) -> Chat){
        retrofit.getChat(token,userId1,userId2).enqueue(
            object : Callback<Chat> {
                override fun onFailure(call: Call<Chat>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse(call: Call<Chat>, response: Response<Chat>) {
                    val chat = response.body()
                    onResult(chat)
                }
            }
        )
    }

    fun getChatsByUserId(userId: String, onResult: (ArrayList<Chat>?) -> ArrayList<Chat>){
        retrofit.getChatsByUserId(userId).enqueue(
            object : Callback<ArrayList<Chat>> {
                override fun onFailure(call: Call<ArrayList<Chat>>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<ArrayList<Chat>>, response: Response<ArrayList<Chat>>) {
                    val chats = response.body()
                    onResult(chats)
                }
            }
        )
    }
}