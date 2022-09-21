package com.example.easy.rest

import android.media.Image
import com.example.easy.data.model.Item
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * @Author Marius Funk
 * Mostly boilerplate therefore uncommented
 */

class ItemRestApiService{

    private val retrofit = ServiceBuilder.buildService(ItemRestApi::class.java)

    fun addItem(token: String, itemData: Item, onResult: (Item?) -> Unit){
        retrofit.addItem(token, itemData).enqueue(
            object : Callback<Item> {
                override fun onFailure(call: Call<Item>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<Item>, response: Response<Item>) {
                    val addedItem = response.body()
                    onResult(addedItem)
                }
            }
        )
    }



    fun getCloseItems(latitude: Double, longditude :Double, range: Int, onResult: (ArrayList<Item>?) -> ArrayList<Item>){
        retrofit.getCloseItems(latitude,longditude,range).enqueue(
            object : Callback<ArrayList<Item>> {
                override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<ArrayList<Item>>, response: Response<ArrayList<Item>>) {
                    val items = response.body()
                    onResult(items)
                }
            }
        )
    }

    //Returns all item inserted by the user specified by the userId
    fun getItemByUserId(userId: String, onResult: (ArrayList<Item>?) -> ArrayList<Item>){
        retrofit.getItemByUserId(userId).enqueue(
            object : Callback<ArrayList<Item>> {
                override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<ArrayList<Item>>, response: Response<ArrayList<Item>>) {
                    val items = response.body()
                    onResult(items)
                }
            }
        )
    }

    //Returns all existing items
    fun getAllItems(onResult: (ArrayList<Item>?) -> ArrayList<Item>){
        retrofit.getAllItems().enqueue(
            object : Callback<ArrayList<Item>> {
                override fun onFailure(call: Call<ArrayList<Item>>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<ArrayList<Item>>, response: Response<ArrayList<Item>>) {
                    val items = response.body()
                    onResult(items)
                }
            }
        )
    }


    fun getItemById(id: String, onResult: (Item?) -> Unit){
        retrofit.getItemById(id).enqueue(
            object : Callback<Item> {
                override fun onFailure(call: Call<Item>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<Item>, response: Response<Item>) {
                    val item = response.body()
                    onResult(item)
                }
            }
        )
    }


    fun changeItem(token: String,itemData: Item, onResult: (Item?) -> Unit){
        retrofit.changeItem(token, itemData).enqueue(
            object : Callback<Item> {
                override fun onFailure(call: Call<Item>, t: Throwable) {
                    onResult(null)
                }
                override fun onResponse( call: Call<Item>, response: Response<Item>) {
                    val changedItem = response.body()
                    onResult(changedItem)
                }
            }
        )
    }


    fun deleteItem(token: String, itemId: String, userId: String, onResult: (Item?) -> Unit) {
        retrofit.deleteItem(token,itemId,userId).enqueue(object : Callback<Item> {
            override fun onFailure(call: Call<Item>, t: Throwable) {
                onResult(null)
            }
            override fun onResponse( call: Call<Item>, response: Response<Item>) {
                val changedItem = response.body()
                onResult(changedItem)
            }
        })
    }

}