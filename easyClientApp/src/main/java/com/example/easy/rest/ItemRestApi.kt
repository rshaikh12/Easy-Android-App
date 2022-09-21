package com.example.easy.rest

import com.example.easy.data.model.Item
import retrofit2.Call
import retrofit2.http.*


/**
 * @Author Marius Funk
 */

interface ItemRestApi {
    /**
     * Adds item and returns said item including its database id
     */
    @Headers("Content-Type: application/json")
    @POST("/item")
    fun addItem(@Header("Authorization") token: String,@Body itemData: Item): Call<Item>

    /**
     * returns items around the specified point by the specified range
     */
    @Headers("Content-Type: application/json")
    @GET("/item")
    fun getCloseItems(@Query("latitude") latitude: Double, @Query("longditude") longditude: Double,
                      @Query("range") range: Int): Call<ArrayList<Item>>


    /**
     * returns all items of the specified user
     */
    @Headers("Content-Type: application/json")
    @GET("/item/findByUserId")
    fun getItemByUserId(@Query("userId") userId: String): Call<ArrayList<Item>>

    /**
     * Returns all items, performance issues expected
     */
    @Headers("Content-Type: application/json")
    @GET("/item/findAll")
    fun getAllItems(): Call<ArrayList<Item>>


    /**
     * Returns single item by its id
     */
    @Headers("Content-Type: application/json")
    @GET("/item/findById")
    fun getItemById(@Query("id") id: String): Call<Item>

    /**
     * Changes one item and returns the updated item
     */
    @Headers("Content-Type: application/json")
    @PUT("/item")
    fun changeItem(@Header("Authorization") token: String, @Body itemData: Item): Call<Item>

    /**
     * Delete the specified item
     */
    @Headers("Content-Type: application/json")
    @DELETE("/item")
    fun deleteItem(@Header("Authorization") token: String, @Query("itemId") itemId: String, @Query("userId") userId: String ): Call<Item>
}