package com.example.easy.data.model

import android.location.Geocoder
import android.media.Image
import com.google.gson.annotations.SerializedName

/**
 * @author Marius Funk
 *
 * items for the application. Image is the string base64 of the bitmap
 *
 */

data class Item(
    @SerializedName("id", alternate=["_id"]) var id: String,
    @SerializedName("name") var name: String,
    @SerializedName("description") var description: String,
    @SerializedName("latitude") var latitude: Double,
    @SerializedName("longditude") var longditude: Double,
    @SerializedName("userId") var userId: String,
    @SerializedName("image") var image: String,
    @SerializedName("categories") var categories: ArrayList<String>,
    @SerializedName("isSold") var isSold: Boolean,
    @SerializedName("timestamp") var timestamp: Long,
)
