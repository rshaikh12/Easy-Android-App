package com.example.easy.ui.mainMenu.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.easy.R
import com.example.easy.data.model.Item
import com.example.easy.databinding.FragmentAdBinding
import com.example.easy.databinding.FragmentAddItemBinding
import com.example.easy.ui.mainMenu.activity.MainMenuActivity
import com.example.easy.ui.mainMenu.adapters.AdAdapter
import com.example.easy.ui.viewmodels.ItemViewModel
import com.example.easy.ui.viewmodels.ItemViewModelFactory
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author Roxana Shaikh, Marius Funk
 */

class ItemFragment : Fragment() {

    private lateinit var addsBtn: FloatingActionButton
    private lateinit var recv: RecyclerView
    //private var userItemList = MutableLiveData<ArrayList<Item>>()
    private var layoutManager: RecyclerView.LayoutManager? = null
    private lateinit var adapter: AdAdapter
    private lateinit var itemViewModel: ItemViewModel

    /**
     * resultLauncher for the openGalerie function
     */
    var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            try{
                val data: Intent? = result.data
                //Prepare image result for database
                itemViewModel.prepareResultForDatabase(
                    data!!.data!!,
                    requireContext(),
                    R.drawable.ic_baseline_add_24
                )

            }
            catch(e: Exception){
                Log.e("IMAGE ERROR: ",e.toString())
            }
        }
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        itemViewModel = ViewModelProvider(this, ItemViewModelFactory())
            .get(ItemViewModel::class.java)
        /**
         * bind view to the Fragment_ad.xml
         */
        val binding = FragmentAdBinding.inflate(layoutInflater)
        binding.itemViewModel = itemViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        itemViewModel.user  = (activity as MainMenuActivity).viewModel.user

        addsBtn = binding.root.findViewById(R.id.addBttn)

        // set recyclerview
        recv = binding.root.findViewById(R.id.FragmentRecycler)


        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Observe the itemlist for updates if changes occur
        itemViewModel.itemList.observe(viewLifecycleOwner, Observer {
            val observedItemList = it ?: return@Observer
            (activity as MainMenuActivity).dataChanged(adapter)
            if(itemViewModel.itemList.value!!.size != observedItemList.size) {
                itemViewModel.itemList.value = observedItemList
                itemViewModel.itemListInternal = observedItemList
            }
        })
        //Get init items
        itemViewModel.getItemsByUserId()

        Log.d("Test", itemViewModel.itemList.value!!.size.toString() + itemViewModel.itemList.value!!.toString())
        adapter = AdAdapter(requireActivity(),itemViewModel,this)
        //Set recycler view
        recv.layoutManager = LinearLayoutManager(requireActivity())
        recv.adapter = adapter


        addsBtn.setOnClickListener {
            addItemDialog(adapter)
        }
    }


    /**
     * display the add item dialog
     */
    private fun addItemDialog(adapter:RecyclerView.Adapter<AdAdapter.ViewHolder>) {

        val addDialog = AlertDialog.Builder(requireActivity())

        // get fragment_add_items binding
        val addItemBinding = FragmentAddItemBinding.inflate(layoutInflater)
        addItemBinding.viewmodel = itemViewModel
        addItemBinding.lifecycleOwner = viewLifecycleOwner
        val v = addItemBinding.root
        addDialog.setView(v)

        //get xml texts for edit
        val title = v.findViewById<EditText>(R.id.item_title)
        val userNo = v.findViewById<EditText>(R.id.Item_text)
        val addImageButton = v.findViewById<ImageButton>(R.id.addImageButton)


        addImageButton.setOnClickListener{
                openGalleryForImage()
        }

        // Get default image
        val imageUri =  Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
            .authority(resources.getResourcePackageName(R.drawable.ic_baseline_add_24))
            .appendPath(resources.getResourceTypeName(R.drawable.ic_baseline_add_24))
            .appendPath(resources.getResourceEntryName(R.drawable.ic_baseline_add_24))
            .build()
        itemViewModel.prepareResultForDatabase(imageUri,requireContext(), R.drawable.ic_baseline_add_24)
        //Set time
        itemViewModel.timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))

        //This is called once the ok button is pressed
        addDialog.setPositiveButton("Ok"){
                dialog,_->
            val itemTitle = title.text.toString()
            val message = userNo.text.toString()


            //Get active image
            val imageString = itemViewModel.activeItemImage.value!!


            // Adds the item in the database
            itemViewModel.addItem(requireContext(),Item("TOBESET","$itemTitle","$message",itemViewModel.user.latitude,itemViewModel.user.longditude,
                itemViewModel.user.userId,imageString, ArrayList<String>(), false,System.currentTimeMillis()))

            //Update AdAdapter
            adapter.notifyDataSetChanged()
            (activity as MainMenuActivity).dataChanged(adapter)
            Toast.makeText(requireActivity(),"Add Information Success",Toast.LENGTH_SHORT).show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel"){
                dialog,_->
            dialog.dismiss()
            Toast.makeText(requireActivity(),"Cancel",Toast.LENGTH_SHORT).show()

        }
        addDialog.create()
        addDialog.show()
    }

    /**
     * opens image gallery
     */
    fun openGalleryForImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        resultLauncher.launch(intent)
    }


    //Update on resume if others have added/deleted
    override fun onResume() {
        super.onResume()
        itemViewModel.getItemsByUserId()
    }
}