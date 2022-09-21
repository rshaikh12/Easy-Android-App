package com.example.easy.ui.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easy.data.model.Chat
import com.example.easy.data.model.ChatEntry
import com.example.easy.data.model.LoggedInUser
import com.example.easy.data.model.LoginResponse
import com.example.easy.rest.ChatRestApiService
import com.example.easy.rest.RestUtil
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
/**
 * @author Roxana Shaikh
 */
class ChatViewModel (): ViewModel(){

    val chatList = MutableLiveData<ArrayList<Chat>>()
    var chatListInternal = ArrayList<Chat>()
    var timeString: String
    var time: Long
    lateinit var user: LoggedInUser

    init{
        chatList.value = ArrayList()
        timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        time = System.currentTimeMillis();
    }

    fun getChatsByUserId() {
        ChatRestApiService().getChatsByUserId(user.userId){
            if(it != null){
                chatListInternal = it
                chatList.postValue(chatListInternal)
                it
            } else{
                ArrayList()
            }
        }
    }

    fun addMessage(ce: ChatEntry, c: Chat){
        ChatRestApiService().addChatMessage(ce, c){
            if(it?.userId != null){
//               //TODO: clear edittext in view and refresh dialog
            }
        }

    }


}