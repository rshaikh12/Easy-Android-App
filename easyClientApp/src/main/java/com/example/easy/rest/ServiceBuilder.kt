package com.example.easy.rest

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * @Author Marius Funk
 *
 * init OkHttpClient and retrofit
 *
 */

object ServiceBuilder {
    private val client = OkHttpClient.Builder().build()

    private const val localUrl3 = "http://192.168.0.216:443/"
    private const val localUrl2 = "http://192.168.0.14:443/"
    private const val localUrl = "http://10.0.2.2:443/"
    private const val productiveUrl = "http://msp-ss2021-5.mobile.ifi.lmu.de:443/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(localUrl) // change this IP for testing by your actual machine IP
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()

    fun<T> buildService(service: Class<T>): T{
        return retrofit.create(service)
    }
}