package com.example.easy.ui.viewmodels

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easy.data.model.Item
import com.example.easy.data.model.LoggedInUser
import com.example.easy.rest.ItemRestApiService
import com.example.easy.rest.RestUtil
import java.lang.Exception
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * @author Marius Funk
 *
 * Contains most logic of the itemFragment
 *
 */

class ItemViewModel (): ViewModel(){

    val itemList = MutableLiveData<ArrayList<Item>>()
    var itemListInternal = ArrayList<Item>()
    val activeItemImageUri = MutableLiveData<String>()
    var activeItemImage = MutableLiveData<String>()
    var timeString: String
    var time: Long
    //Either active user or selected user
    lateinit var user: LoggedInUser


    init{
        itemList.value = ArrayList()
        //Default image
        activeItemImageUri.value = "@drawable/ic_baseline_add_24"
        //Time format
        timeString = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))
        time = System.currentTimeMillis();
    }

    /**
     * Writes the item to the backend and updates the observed list upon return
     */
    fun addItem(context: Context, item: Item) {
        ItemRestApiService().addItem(RestUtil.getToken(context), item){
            if(it?.id != null){
                print("Item written")
                itemListInternal.add(it!!)
                itemList.value = itemListInternal
            } else{

            }
        }
    }

    /**
     *
     * updates an item in the backend and updates the entry in the observed list
     *
     */
    fun updateItem(context: Context, item: Item, adapterPosition: Int){
        ItemRestApiService().changeItem(RestUtil.getToken(context),item){
            if(it?.id != null){
                print("Item updated")
                itemListInternal[adapterPosition] = it!!
                itemList.value = itemListInternal
            } else{

            }
        }
    }

    /**
     * UNUSED
     * returns all items and updates the observed list
     */
    fun getAllItems() {
        ItemRestApiService().getAllItems(){
            if(it != null){
                itemListInternal = it
                itemList.postValue(itemListInternal)
                it
            } else{
                ArrayList()
            }
        }
    }

    /**
     * get all items of the selected user and updates the observed list
     */
    fun getItemsByUserId() {
        ItemRestApiService().getItemByUserId(user.userId){
            if(it != null){
                itemListInternal = it
                itemList.postValue(itemListInternal)
                it
            } else{
                ArrayList()
            }
        }
    }

    /**
     * Prepares the image for the Database
     */
    fun prepareResultForDatabase(data: Uri, context: Context, resource: Int) {
        activeItemImageUri.value = data.toString()
        var bitmap: Bitmap
        try{
            if(!data.toString().contains("android.resource")) {
                //Use RestUtil for the conversion to base64 String
                val source = ImageDecoder.createSource(context.contentResolver, data)
                bitmap = ImageDecoder.decodeBitmap(source)
            } else{
                throw Exception("Is drawable")
            }
        }
        catch (e: Exception){
            //Not best practice but this safes the default picture as the image if no image selected
            val d = context.resources.getDrawable(resource, null)
            bitmap = RestUtil.drawableToBitmap(d)!!
        }
        //set activeItemImage for the item, will be safed later
        activeItemImage.value =  RestUtil.convertToBase64(bitmap)
    }

    /**
     * deletes specified item and updates the list
     */
    fun deleteItem(c: Context, item: Item, adapterPosition: Int) {
        ItemRestApiService().deleteItem(RestUtil.getToken(c), item.id, user.userId){
            if(it != null){
                itemListInternal.remove(itemListInternal[adapterPosition])
                itemList.value = itemListInternal
            } else{

            }
        }
    }
}