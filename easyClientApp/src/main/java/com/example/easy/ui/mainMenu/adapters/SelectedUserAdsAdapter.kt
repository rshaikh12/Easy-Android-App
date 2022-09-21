package com.example.easy.ui.mainMenu.adapters

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.example.easy.R
import com.example.easy.data.model.Item

import com.example.easy.databinding.FragmentItemViewDialogBinding
import com.example.easy.rest.ProfileRestApiService
import com.example.easy.rest.RestUtil

import com.example.easy.ui.mainMenu.fragments.SelectedUserItemsFragment
import com.example.easy.ui.viewmodels.ItemViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author Roxana Shaikh, Marius Funk
 *
 * Similar to the AdAdapter, but handles all items of the selected user
 *
 */
class SelectedUserAdsAdapter(
    val c: Context,
    itemViewModel: ItemViewModel,
    selectedUserItemsFragment: SelectedUserItemsFragment
) : RecyclerView.Adapter<SelectedUserAdsAdapter.ViewHolder>() {

    private val itemViewModel = itemViewModel
    private val allItemsFragment = selectedUserItemsFragment

    inner class ViewHolder(val v: View, adAdapter: SelectedUserAdsAdapter) : RecyclerView.ViewHolder(v) {
        var name: TextView
        var mbNum: TextView
        var mMenus: ImageView

        init {
            name = v.findViewById<TextView>(R.id.mUser)
            mbNum = v.findViewById<TextView>(R.id.mlastMessage)
            mMenus = v.findViewById(R.id.mChat)
            mMenus.setOnClickListener { viewItemDialog(this) }
        }

        /**
         * displays the item dialog for the selected item using AlertDialog like for the users items
         */
        private fun viewItemDialog(adapter: ViewHolder) {
            val position = itemViewModel.itemList.value!![adapterPosition]

            val itemViewDialog = android.app.AlertDialog.Builder(c)


            val viewItemBinding = FragmentItemViewDialogBinding.inflate(allItemsFragment.layoutInflater)
            viewItemBinding.itemViewModel = itemViewModel
            viewItemBinding.lifecycleOwner = allItemsFragment.viewLifecycleOwner
            val v = viewItemBinding.root
            itemViewDialog.setView(v)

            //Set properties for the item
            val image = v.findViewById<ImageView>(R.id.imageView)
            val title = v.findViewById<TextView>(R.id.titleText)
            val description = v.findViewById<TextView>(R.id.descriptionText)
            val userId = v.findViewById<TextView>(R.id.userIdText)

            //Set user name
            ProfileRestApiService().getProfile(position.userId){
                if(it != null){
                    userId.text = it!!.displayName
                }
            }

            setImageForItem(position,allItemsFragment.resources)


            title.text = position.name
            description.text = position.description
            image.setImageURI(Uri.parse(itemViewModel.activeItemImageUri.value))



            itemViewDialog.setPositiveButton("Ok") { dialog, _ ->
                //Close dialog
                dialog.dismiss()
            }

            itemViewDialog.create()
            itemViewDialog.show()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(v, this)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newList = itemViewModel.itemList.value!![position]
        holder.name.text = newList.name
        holder.mbNum.text = newList.description
    }

    override fun getItemCount(): Int {
        return itemViewModel.itemList.value!!.size
    }


    /**
     * Sets the image to be displayed in the pop up dialog
     */
    private fun setImageForItem(
        position: Item,
        resources: Resources
    ) {
        var imageUri: Uri
        if (position.image == null) {
            //If no Image registered, use default + button
            imageUri = Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.ic_baseline_add_24))
                .appendPath(resources.getResourceTypeName(R.drawable.ic_baseline_add_24))
                .appendPath(resources.getResourceEntryName(R.drawable.ic_baseline_add_24))
                .build()
        } else {
            //Convert image
            val bitmapImage = RestUtil.convertToBitmap(position.image)!!

            //Create file in cache and safe bitmap image there for this view
            val file = File(c.cacheDir, "ImageForDisplay") //Get Access to a local file.
            file.delete() // Delete the File, just in Case, that there was still another File
            file.createNewFile()
            val fileOutputStream = file.outputStream()
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val bytearray = byteArrayOutputStream.toByteArray()
            fileOutputStream.write(bytearray)
            fileOutputStream.flush()
            fileOutputStream.close()
            byteArrayOutputStream.close()

            val imageURI = file.toURI()
            imageUri = Uri.parse(imageURI.toString())

        }

        //Set image and time
        itemViewModel.activeItemImageUri.value = imageUri.toString()
        itemViewModel.prepareResultForDatabase(imageUri, c, R.drawable.ic_baseline_add_24)
    }
}