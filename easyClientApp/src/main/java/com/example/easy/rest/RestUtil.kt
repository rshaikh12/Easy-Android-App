package com.example.easy.rest

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import java.io.ByteArrayOutputStream
import java.util.*

/**
 * @Author Marius Funk
 */

object RestUtil {

    //Return the saved token for the logged-in User
    fun getToken(context: Context) : String{
        val sessionManager = SessionManager(context)
        return "Bearer ${sessionManager.fetchAuthToken()}"
    }

    /**
     * Converts a bitmap image to a base 64 String
     *
     * Also resizes the bitmap to a smaller resolution by calling "getResizedBitmap"
     *
     */
    fun convertToBase64(bitmap: Bitmap): String? {
        var smallBitmap = getResizedBitmap(bitmap, 500)!!
        val outputStream = ByteArrayOutputStream()
        smallBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        return Base64.getEncoder().encodeToString(outputStream.toByteArray())
    }


    /**
     * Converts a base 64 String to a bitmap image
     */
    fun convertToBitmap(base64String: String): Bitmap? {
        val decodedBytes: ByteArray = Base64.getDecoder().decode(
            base64String.substring(base64String.indexOf(",") + 1)
        )

        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
    }


    /**
     * Converts a drawable resource to a Bitmap
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        val bitmap =
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight())
        drawable.draw(canvas)
        return bitmap
    }

    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap? {
        var width = image.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }
}