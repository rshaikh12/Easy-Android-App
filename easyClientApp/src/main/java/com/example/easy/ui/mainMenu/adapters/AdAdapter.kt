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
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.PopupMenu
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.example.easy.R
import com.example.easy.data.model.Item
import com.example.easy.databinding.FragmentAddItemBinding
import com.example.easy.rest.RestUtil
import com.example.easy.ui.mainMenu.activity.MainMenuActivity
import com.example.easy.ui.mainMenu.fragments.ItemFragment
import com.example.easy.ui.viewmodels.ItemViewModel
import java.io.ByteArrayOutputStream
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author Roxana Shaikh, Marius Funk
 *
 * This class handles the recyclerview for the user items
 *
 */
class AdAdapter(val c: Context, itemViewModel: ItemViewModel, itemFragment: ItemFragment) :
    RecyclerView.Adapter<AdAdapter.ViewHolder>() {

    private val itemViewModel = itemViewModel
    private val itemFragment = itemFragment

    inner class ViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        var name: TextView
        var mbNum: TextView
        var mMenus: ImageView

        init {
            name = v.findViewById<TextView>(R.id.mUser)
            mbNum = v.findViewById<TextView>(R.id.mlastMessage)
            mMenus = v.findViewById(R.id.mChat)
            mMenus.setOnClickListener { popupMenus(it) }
        }

        /**
         * Handles all logic when item is chosen to be deleted or modified
         */
        private fun popupMenus(v: View) {
            val position = itemViewModel.itemList.value!![adapterPosition]
            val popupMenus = PopupMenu(c, v)
            popupMenus.inflate(R.menu.show_menu)
            popupMenus.setOnMenuItemClickListener {
                when (it.itemId) {

                    R.id.editText -> {
                        /**
                         * Handels a changed item
                         */
                        modifyItemDialog(
                            itemFragment.requireActivity(),
                            itemFragment.layoutInflater,
                            itemFragment.viewLifecycleOwner,
                            itemFragment.resources,
                            position,
                            adapterPosition
                        )

                        true
                    }
                    R.id.delete -> {
                        /**
                         * set item to be deleted
                         */
                        AlertDialog.Builder(c)
                            .setTitle("Delete")
                            .setMessage("Are you sure")
                            .setPositiveButton("Yes") { dialog, _ ->
                                val item = position
                                itemViewModel.deleteItem(c,item,adapterPosition)

                                notifyDataSetChanged()
                                Toast.makeText(c, "Changed this Information", Toast.LENGTH_SHORT)
                                    .show()
                                dialog.dismiss()
                            }
                            .setNegativeButton("No") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .create()
                            .show()

                        true
                    }
                    else -> true
                }

            }
            popupMenus.show()
            val popup = PopupMenu::class.java.getDeclaredField("mPopup")
            popup.isAccessible = true
            val menu = popup.get(popupMenus)
            menu.javaClass.getDeclaredMethod("setForceShowIcon", Boolean::class.java)
                .invoke(menu, true)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(v)
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
     * Handels the logic for the modification of items
     */
    fun modifyItemDialog(
        requireActivity: FragmentActivity,
        layoutInflater: LayoutInflater,
        viewLifecycleOwner: LifecycleOwner,
        resources: Resources,
        position: Item,
        adapterPosition: Int
    ) {
        //prepare image for display
        setImageForItem(position, resources)

        //This is the item dialog
        val addDialog = android.app.AlertDialog.Builder(requireActivity)
        //Normal item setup
        val addItemBinding = FragmentAddItemBinding.inflate(layoutInflater)
        addItemBinding.viewmodel = itemViewModel
        addItemBinding.lifecycleOwner = viewLifecycleOwner
        val v = addItemBinding.root
        addDialog.setView(v)

        val title = v.findViewById<EditText>(R.id.item_title)
        title.setText(position.name)
        val userNo = v.findViewById<EditText>(R.id.Item_text)
        userNo.setText(position.description)
        val addImageButton = v.findViewById<ImageButton>(R.id.addImageButton)

        //Add button on click to change
        addImageButton.setOnClickListener {
            itemFragment.openGalleryForImage()
        }



        addDialog.setPositiveButton("Ok") { dialog, _ ->
            /**
             * Update item with result from update dialog
             */
            val item = Item(
                position.id,
                position.name,
                position.description,
                position.latitude,
                position.longditude,
                position.userId,
                itemViewModel.activeItemImage.value!!,
                position.categories,
                position.isSold,
                System.currentTimeMillis()
            )
            itemViewModel.updateItem(c, item, adapterPosition)

            this.notifyDataSetChanged()
            (itemFragment.activity as MainMenuActivity).dataChanged(this)
            Toast.makeText(itemFragment.activity, "Add Information Success", Toast.LENGTH_SHORT)
                .show()
            dialog.dismiss()
        }
        //Will dismiss all changes
        addDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(itemFragment.activity, "Cancel", Toast.LENGTH_SHORT).show()

        }
        addDialog.create()
        addDialog.show()
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
        itemViewModel.timeString =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    }
}