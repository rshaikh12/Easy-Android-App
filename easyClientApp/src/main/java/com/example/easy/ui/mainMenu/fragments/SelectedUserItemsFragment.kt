package com.example.easy.ui.mainMenu.fragments


import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easy.R
import com.example.easy.databinding.FragmentAllAdsBinding
import com.example.easy.ui.mainMenu.activity.MainMenuActivity
import com.example.easy.ui.mainMenu.adapters.SelectedUserAdsAdapter
import com.example.easy.ui.viewmodels.ItemViewModel
import com.example.easy.ui.viewmodels.ItemViewModelFactory
import java.lang.Exception

/**
 * @author Marius Funk, Roxana Shaikh
 */

class SelectedUserItemsFragment : Fragment() {

    private lateinit var recv: RecyclerView
    private lateinit var adapter: SelectedUserAdsAdapter


    private lateinit var itemViewModel: ItemViewModel


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {



        itemViewModel = ViewModelProvider(this, ItemViewModelFactory())
            .get(ItemViewModel::class.java)

        //Set binding
        val binding = FragmentAllAdsBinding.inflate(layoutInflater)
        binding.itemViewModel = itemViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        itemViewModel.user  = (activity as MainMenuActivity).viewModel.chosenUser.value!!


        //Set recycling view
        recv = binding.root.findViewById(R.id.FragmentRecycler)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // observe item list and change items
        itemViewModel.itemList.observe(viewLifecycleOwner, Observer {
            val observedItemList = it ?: return@Observer
            (activity as MainMenuActivity).allDataChanged(adapter)
            if(itemViewModel.itemList.value!!.size != observedItemList.size) {
                itemViewModel.itemList.value = observedItemList
                itemViewModel.itemListInternal = observedItemList
            }
        })

        //observe the selected user to update the whole list
        (requireActivity() as MainMenuActivity).viewModel.chosenUser.observe(viewLifecycleOwner, Observer {
            val observedUser = it ?: return@Observer
            itemViewModel.user = observedUser
            itemViewModel.getItemsByUserId()
        })

        //Set initial selected users items
        itemViewModel.getItemsByUserId()

        Log.d("Test", itemViewModel.itemList.value!!.size.toString() + itemViewModel.itemList.value!!.toString())
        adapter = SelectedUserAdsAdapter(requireActivity(),itemViewModel,this)
        recv.layoutManager = LinearLayoutManager(requireActivity())
        recv.adapter = adapter



    }

    override fun onResume() {
        super.onResume()
        itemViewModel.getItemsByUserId()
    }
}