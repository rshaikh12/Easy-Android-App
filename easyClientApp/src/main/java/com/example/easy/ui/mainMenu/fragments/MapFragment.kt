package com.example.easy.ui.mainMenu.fragments

import android.Manifest
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import androidx.lifecycle.Observer
import com.example.easy.R
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import android.content.pm.PackageManager
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.easy.databinding.FragmentMapBinding
import com.example.easy.ui.mainMenu.activity.MainMenuActivity
import com.example.easy.ui.viewmodels.MapViewModel
import com.example.easy.ui.viewmodels.MapViewModelFactory
import com.google.android.gms.maps.*

/**
 * @author Iuliia Khobotova, Roxana Shaikh, Marius Funk
 */

class MapFragment : Fragment(), GoogleMap.OnMarkerClickListener, OnMapReadyCallback {


    private lateinit var mapViewModel: MapViewModel
    private val LOCATION_PERMISSION_REQUEST = 1



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mapViewModel = ViewModelProvider(this, MapViewModelFactory())
            .get(MapViewModel::class.java)

        //Add binding to xml
        val binding = FragmentMapBinding.inflate(layoutInflater)
        binding.mapViewModel = mapViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        //this is the selected user for the selected user items
        mapViewModel.user  = (activity as MainMenuActivity).viewModel.chosenUser.value!!


        //initiate the map
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync {
                googleMap -> mapViewModel.mMap = googleMap
            //Get access
            getLocationAccess()
            mapViewModel.mapReady = true
            //additional steps
            onMapReady(mapViewModel.mMap)


        }



        //Update user location
        mapViewModel.updateUserLocation(context)



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //Observe if the values of the users change
        mapViewModel.userList.observe(viewLifecycleOwner, Observer {
            val observedItemList = it ?: return@Observer
            mapViewModel.updateMap()

            if(mapViewModel.userList.value!!.size != observedItemList.size) {
                mapViewModel.userList.value = observedItemList
                mapViewModel.userListInternal = observedItemList
            }
        })

        mapViewModel.getAllUsers()
    }


    /**
     * determines wheater locations are enabled and requests if not
     */
    private fun getLocationAccess() {
        if (ContextCompat.checkSelfPermission(this.requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            mapViewModel.mMap.isMyLocationEnabled = true
        }
        else {
            ActivityCompat.requestPermissions(
                this.requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST
            )
        }

    }

    /**
     * handels permission request result
     */
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.contains(PackageManager.PERMISSION_GRANTED)) {
                if (ActivityCompat.checkSelfPermission(
                        this.requireContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this.requireContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }

                //enables location and sets user
                mapViewModel.mMap.isMyLocationEnabled = true
                getLocationAccess()
                mapViewModel.updateUserLocation(requireContext())
            }
            else {

                Toast.makeText(this.requireContext(), "User has not granted location access permission", LENGTH_LONG).show()
            }
        }
    }


    /**
     * Update selected user on marker click
     */
    override fun onMarkerClick(p0: Marker?): Boolean {

        mapViewModel.chosenUser = mapViewModel.markerObjectMap.getOrDefault(p0!!, mapViewModel.user)
        (activity as MainMenuActivity).viewModel.chosenUser.value = mapViewModel.chosenUser
        return false
    }


    override fun onResume() {
        super.onResume()
        mapViewModel.getAllUsers()
        mapViewModel.updateUserLocation(requireContext())
    }

    /**
     * when map ready set markers and get device location
     */
    override fun onMapReady(p0: GoogleMap?) {
        mapViewModel.mMap = p0!!

        mapViewModel.mMap.setOnMarkerClickListener(this)
        mapViewModel.getDeviceLocation(requireContext())

    }



}




