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
 * @author Roxana Shaikh
 */
class ChatAdapter(val c: Context, itemViewModel: ItemViewModel, itemFragment: ItemFragment) :
    RecyclerView.Adapter<ChatAdapter.ViewHolder>() {

    private val itemViewModel = itemViewModel
    private val itemFragment = itemFragment

    inner class ViewHolder(val v: View, adAdapter: ChatAdapter) : RecyclerView.ViewHolder(v) {
        var name: TextView
        var mbNum: TextView
        var mMenus: ImageView

        init {
            name = v.findViewById<TextView>(R.id.mUser)
            mbNum = v.findViewById<TextView>(R.id.mlastMessage)
            mMenus = v.findViewById(R.id.mChat)
            mMenus.setOnClickListener { popupMenus(it) }
        }

        private fun popupMenus(v: View) {
            val position = itemViewModel.itemList.value!![adapterPosition]
            val popupMenus = PopupMenu(c, v)
            popupMenus.inflate(R.menu.show_menu)
            popupMenus.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.editText -> {

                        modifyItemDialog(
                            itemFragment.requireActivity(),
                            itemFragment.layoutInflater,
                            itemFragment.viewLifecycleOwner,
                            itemFragment.resources,
                            position,
                            adapterPosition
                        )
                        /*
                        val v = LayoutInflater.from(c).inflate(R.layout.add_item,null)
                        val name = v.findViewById<EditText>(R.id.userName)
                        val number = v.findViewById<EditText>(R.id.userNo)
                        AlertDialog.Builder(c)
                            .setView(v)
                            .setPositiveButton("Ok"){
                                    dialog,_->
                                position.name = name.text.toString()
                                position.description= number.text.toString()
                                //TODO updateItem neues Layout! mehr updates!
                                val item = Item(position.id, position.name, position.description, position.latitude,
                                    position.longditude,position.userId,position.image,position.categories, position.isSold,
                                    System.currentTimeMillis())
                                itemViewModel.updateItem(c, item)

                                notifyDataSetChanged()
                                Toast.makeText(c,"User Information is Edited",Toast.LENGTH_SHORT).show()
                                dialog.dismiss()

                            }
                            .setNegativeButton("Cancel"){
                                    dialog,_->
                                dialog.dismiss()

                            }
                            .create()
                            .show() */

                        true
                    }
                    R.id.delete -> {
                        /**set delete*/
                        AlertDialog.Builder(c)
                            .setTitle("Set to sold")
                            //.setIcon(R.drawable.ic_warning)
                            .setMessage("Are you sure")
                            .setPositiveButton("Yes") { dialog, _ ->
                                //TODO Momentan nur über isSold
                                val item = position
                                item.isSold = true
                                itemViewModel.updateItem(c, item, adapterPosition)
                                //itemViewModel.itemList.value!!.removeAt(adapterPosition)
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


    fun modifyItemDialog(
        requireActivity: FragmentActivity,
        layoutInflater: LayoutInflater,
        viewLifecycleOwner: LifecycleOwner,
        resources: Resources,
        position: Item,
        adapterPosition: Int
    ) {

        setImageForItem(position, resources)

        val addDialog = android.app.AlertDialog.Builder(requireActivity)

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


        addImageButton.setOnClickListener {
            itemFragment.openGalleryForImage()
        }



        addDialog.setPositiveButton("Ok") { dialog, _ ->

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
            //(itemFragment.activity as MainMenuActivity).dataChanged()
            Toast.makeText(itemFragment.activity, "Add Information Success", Toast.LENGTH_SHORT)
                .show()
            dialog.dismiss()
        }
        addDialog.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
            Toast.makeText(itemFragment.activity, "Cancel", Toast.LENGTH_SHORT).show()

        }
        addDialog.create()
        addDialog.show()
    }

    private fun setImageForItem(
        position: Item,
        resources: Resources
    ) {
        var imageUri: Uri
        if (position.image == null) {
            imageUri = Uri.Builder()
                .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                .authority(resources.getResourcePackageName(R.drawable.ic_baseline_add_24))
                .appendPath(resources.getResourceTypeName(R.drawable.ic_baseline_add_24))
                .appendPath(resources.getResourceEntryName(R.drawable.ic_baseline_add_24))
                .build()
        } else {

            val bitmapImage = RestUtil.convertToBitmap(position.image)!!

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


        itemViewModel.activeItemImageUri.value = imageUri.toString()
        itemViewModel.prepareResultForDatabase(imageUri, c, R.drawable.ic_baseline_add_24)
        itemViewModel.timeString =
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
    }
}