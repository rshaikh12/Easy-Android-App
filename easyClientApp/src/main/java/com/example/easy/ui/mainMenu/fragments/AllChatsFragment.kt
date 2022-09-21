package com.example.easy.ui.mainMenu.fragments


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easy.R
import com.example.easy.data.model.Chat
import com.example.easy.databinding.FragmentAllChatsBinding
import com.example.easy.ui.mainMenu.activity.MainMenuActivity
import com.example.easy.ui.mainMenu.adapters.AllChatsAdapter
import com.example.easy.ui.viewmodels.ChatViewModel
import com.example.easy.ui.viewmodels.ChatViewModelFactory
/**
 * @author Roxana Shaikh
 */

class AllChatsFragment : Fragment() {

    private lateinit var recv: RecyclerView
    private lateinit var chatList: ArrayList<Chat>
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var adapter: AllChatsAdapter


//    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
//        if (result.resultCode == Activity.RESULT_OK) {
//            try{
//                val data: Intent? = result.data
//                chatViewModel.prepareResultForDatabase(
//                    data!!.data!!,
//                    requireContext(),
//                    R.drawable.ic_baseline_add_24
//                )
//
//            }
//            catch(e: Exception){
//                Log.e("IMAGE ERROR: ",e.toString())
//            }
//        }
//    }

    private lateinit var chatViewModel: ChatViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        chatViewModel = ViewModelProvider(this, ChatViewModelFactory())
            .get(ChatViewModel::class.java)

        val binding = FragmentAllChatsBinding.inflate(layoutInflater)
        binding.chatViewModel = chatViewModel
        binding.lifecycleOwner = viewLifecycleOwner



        // Inflate the layout for this fragment
        //var view: View = inflater.inflate(R.layout.fragment_ad, container, false)

        chatViewModel.user  = (activity as MainMenuActivity).viewModel.user

        recv = binding.root.findViewById(R.id.Recycler)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        chatViewModel.chatList.observe(viewLifecycleOwner, Observer {
            val observedItemList = it ?: return@Observer
            (activity as MainMenuActivity).allChatDataChanged(adapter)
            if(chatViewModel.chatList.value!!.size != observedItemList.size) {
                chatViewModel.chatList.value = observedItemList
                chatViewModel.chatListInternal = observedItemList
            }
        })

        chatViewModel.getChatsByUserId()

        Log.d("Test", chatViewModel.chatList.value!!.size.toString() + chatViewModel.chatList.value!!.toString())
        adapter = AllChatsAdapter(requireActivity(),chatViewModel,this)
        recv.layoutManager = LinearLayoutManager(requireActivity())
        recv.adapter = adapter



    }



}