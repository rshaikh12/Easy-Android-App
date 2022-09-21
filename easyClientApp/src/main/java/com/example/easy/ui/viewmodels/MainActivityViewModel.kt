package com.example.easy.ui.viewmodels


import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easy.data.model.LoggedInUser
import com.google.gson.Gson

/**
 * @author Marius Funk
 */

class MainActivityViewModel() : ViewModel() {


    var chosenUser = MutableLiveData<LoggedInUser>()
    lateinit var user: LoggedInUser

    //Sets user position around munich if no position allowed/available for the database
    fun setUser(stringExtra: String) {
        var userJson =  stringExtra
        var gson = Gson()
        user = gson.fromJson(userJson, LoggedInUser::class.java)
        if(user.latitude == null){
            user.latitude = 48.137079 + (-1..1).random()
            user.longditude = 11.576006 + (-1..1).random()
        }


        chosenUser.value = user
    }
}



