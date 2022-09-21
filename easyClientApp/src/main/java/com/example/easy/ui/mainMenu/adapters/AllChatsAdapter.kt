package com.example.easy.ui.mainMenu.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easy.R
import com.example.easy.data.model.ChatEntry
import com.example.easy.data.model.Item
import com.example.easy.databinding.FragmentChatBinding
import com.example.easy.ui.mainMenu.activity.MainMenuActivity
//import com.example.easy.databinding.FragmentAllChatsBinding
import com.example.easy.ui.mainMenu.fragments.AllChatsFragment
import com.example.easy.ui.viewmodels.ChatViewModel
/**
 * @author Roxana Shaikh
 */

class AllChatsAdapter(
    val c: Context,
    chatViewModel: ChatViewModel,
    allChatsFragment: AllChatsFragment
) : RecyclerView.Adapter<AllChatsAdapter.ViewHolder>() {

    private val chatViewModel = chatViewModel
    private val allChatsFragment = allChatsFragment

    inner class ViewHolder(val v: View, chatAdapter: AllChatsAdapter) : RecyclerView.ViewHolder(v) {
        var mUser: TextView
        var mlastMessage: TextView
        var mChat: ImageView



        init {
            mUser = v.findViewById<TextView>(R.id.mUser)
            mlastMessage = v.findViewById<TextView>(R.id.mlastMessage)
            mChat = v.findViewById<ImageView>(R.id.mChat)
            mChat.setOnClickListener { viewItemDialog(this) }
        }

        private fun viewItemDialog(adapter: AllChatsAdapter.ViewHolder) {
            val position = chatViewModel.chatList.value!![adapterPosition]

            val chatViewDialog = android.app.AlertDialog.Builder(c)




            val viewItemBinding = FragmentChatBinding.inflate(allChatsFragment.layoutInflater)
            viewItemBinding.chatViewModel = chatViewModel
            viewItemBinding.lifecycleOwner = allChatsFragment.viewLifecycleOwner
            val v = viewItemBinding.root
            chatViewDialog.setView(v)

            val myUserId = chatViewModel.user.userId

            val sendBtn = v.findViewById<Button>(R.id.sendMessageButton)

            val chatInputText = v.findViewById<EditText>(R.id.chatInputText)

            val title = v.findViewById<TextView>(R.id.chatActivityTitle)
            if (myUserId == position.userId1) {
                title.text = position.userId2

            } else {
                title.text = position.userId1
            }
//            val bubbles = []
            val chats : ArrayList<ChatEntry> = position.chatEntries
            chats.forEach {
                val chat  : ChatEntry = it
                if (chat.userId == myUserId) {
                    //TODO: style 1
                } else {
                    //TODO: style 2
                }
            }

//            val description = v.findViewById<TextView>(R.id.descriptionText)
//            val userId = v.findViewById<TextView>(R.id.userIdText)
//            val contactBtn = v.findViewById<Button>(R.id.contact)
//
//            title.text = position.name
//            description.text = position.description
//            userId.text = position.userId

            sendBtn.setOnClickListener {
                val msg: String = chatInputText.text.toString()

                //check if the EditText have values or not
                if(msg.trim().length>0) {
                    val chatEntry = ChatEntry(
                        msg,
                        myUserId,
                    )

                    chatViewModel.addMessage(chatEntry, position)

                    notifyDataSetChanged()

                }
            }


            chatViewDialog.setPositiveButton("Ok") { dialog, _ ->

                dialog.dismiss()
            }

            chatViewDialog.create()
            chatViewDialog.show()
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_chat, parent, false)
        return ViewHolder(v, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newList = chatViewModel.chatList.value!![position]
        val myUserId = chatViewModel.user.userId
        val user1 = newList.userId1
        var user2 = newList.userId2

        if (myUserId == user1) {
            holder.mUser.text = user2
        } else {
            holder.mUser.text = user1
        }

        val lastMessage = newList.chatEntries!!.last()
        if (lastMessage.userId == myUserId) {
            holder.mlastMessage.text = "✔️ " + newList.chatEntries!!.last().message
        } else {
            holder.mlastMessage.text = newList.chatEntries!!.last().message
        }

    }

    override fun getItemCount(): Int {
        return chatViewModel.chatList.value!!.size
    }
}