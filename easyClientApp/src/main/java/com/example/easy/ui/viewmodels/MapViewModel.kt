package com.example.easy.ui.viewmodels


import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.easy.data.model.Item
import com.example.easy.data.model.LoggedInUser
import com.example.easy.rest.ItemRestApiService
import com.example.easy.rest.ProfileRestApiService
import com.example.easy.rest.RestUtil
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
/**
 * @author Marius Funk
 */
class MapViewModel (): ViewModel(){


    val userList = MutableLiveData<ArrayList<LoggedInUser>>()
    var userListInternal = ArrayList<LoggedInUser>()
    var time: Long
    lateinit var user: LoggedInUser
    lateinit var chosenUser : LoggedInUser
    var markerObjectMap = HashMap<Marker,LoggedInUser>()

    var mapReady = false
    lateinit var mMap : GoogleMap

    lateinit var mFusedLocationProviderClient : FusedLocationProviderClient

    init{

        userList.value = ArrayList()
        time = System.currentTimeMillis();
    }

    /**
     * clear and the refill map with markers on update
     */
    fun updateMap() {
        if (mapReady && userList != null) {
            markerObjectMap.clear()
            mMap.clear()
            userList.value!!.forEach {
                    user ->
                if (user.longditude!=null && user.latitude!=null && user.userId != this.user.userId) {
                    val marker = LatLng(user.latitude, user.longditude)
                    val markerObject = mMap.addMarker(MarkerOptions().position(marker).title(user.displayName.toString()))
                    markerObjectMap.put(markerObject,user)
                }
            }
        }
    }

    /**
     * update or set location if no location is found
     */
    fun updateUserLocation(context: Context?) {
        if(user.latitude == null || (user.latitude == 0.0 && user.longditude == 0.0)){
            user.latitude = 48.137079 + (-1..1).random()
            user.longditude = 11.576006  + (-1..1).random()
        }
        ProfileRestApiService().changeProfile(RestUtil.getToken(context!!),user){
            if(it != null){
                user.longditude = it.longditude
                user.latitude = it.latitude
                moveCamera(
                    LatLng(user.longditude, user.latitude),
                    10f
                )
            } else{

            }
        }
    }

    /**
     * Get all users to display on map
     */
    fun getAllUsers(){
        ProfileRestApiService().getAllProfiles(){
            if(it != null){
                userListInternal = it
                userList.postValue(userListInternal)
            } else{
                ArrayList<LoggedInUser>()
            }
        }
    }

    /**
     * Get the device location
     */
    fun getDeviceLocation(context: Context){
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        try{
            //If location is enables
            if(mMap.isMyLocationEnabled){
                val location = mFusedLocationProviderClient.lastLocation
                location.addOnCompleteListener {
                    if (it.isSuccessful) {
                        //Set location and zoom on location find
                        Log.d("LOCATION", "Location found")
                        var currentLocation = it.result
                        moveCamera(
                            LatLng(currentLocation!!.latitude, currentLocation!!.longitude),
                            10f
                        )
                        user.latitude = currentLocation!!.latitude
                        user.longditude = currentLocation!!.longitude
                        updateUserLocation(context)
                    } else {
                        Toast.makeText(
                            context,
                            "Unable to get current location",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }catch(e : SecurityException){
            moveCamera(LatLng( 	48.137079, 	11.576006),5f)
        }
    }

    /**
     * simple method to move camera
     */
    fun moveCamera(latLng: LatLng, zoom : Float){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,zoom))
    }

}