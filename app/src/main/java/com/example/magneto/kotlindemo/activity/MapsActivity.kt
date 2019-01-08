package com.example.magneto.kotlindemo.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.magneto.kotlindemo.R
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.GeoDataClient
import com.google.android.gms.location.places.PlaceDetectionClient
import com.google.android.gms.location.places.Places
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener {


    private lateinit var mMap: GoogleMap
    private lateinit var mGoogleApiClient:GoogleApiClient
    private lateinit var mGeoDataClient:GeoDataClient
    private lateinit var mPlaceDetectionClient:PlaceDetectionClient
    private lateinit var mFusedLocationProviderClient:FusedLocationProviderClient
    private var mLastKnownLocation:Location?=null

    private lateinit var mContent:Context

    private var isLocationPermissionGranted=false
    private var PERMISSION_REQUEST_LOCATION=10
    private var DEFAULT_ZOOM=15.0f
    private var USER_ZOOM=DEFAULT_ZOOM
    private var isZooming=false

    private val sydney = LatLng(-34.0, 151.0)

    var bitmapDescriptor:BitmapDescriptor? =null

    private var iv_gps:ImageView?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_new)

        mContent=this
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getLocationPermission()

        mGeoDataClient=Places.getGeoDataClient(this)

        mPlaceDetectionClient=Places.getPlaceDetectionClient(this)

        mFusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)

        mGoogleApiClient=GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build()

        iv_gps=findViewById(R.id.iv_gps)
        bitmapDescriptor=BitmapDescriptorFactory.fromResource(R.drawable.ic_gps_individual)
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContent,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                        ==PackageManager.PERMISSION_GRANTED){

//            permission has been granted
            isLocationPermissionGranted=true
        }
        else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),PERMISSION_REQUEST_LOCATION)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        isLocationPermissionGranted=false

        when(requestCode){
            PERMISSION_REQUEST_LOCATION->{
                if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    isLocationPermissionGranted=true
                }
            }
        }
        updateLocationUI()
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.uiSettings.isMyLocationButtonEnabled=true
        mMap.uiSettings.isRotateGesturesEnabled=false
        mMap.uiSettings.isScrollGesturesEnabled=true


        mMap.setOnCameraIdleListener(this)
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);


        updateLocationUI()

        getDeviceLocation()
        // Add a marker in Sydney and move the camera


//        val currentLocation = LatLng(mLastKnownLocation!!.latitude,mLastKnownLocation!!.longitude)
        Log.e("Loaction",sydney.toString())
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker at Sydney")).setIcon(bitmapDescriptor)

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun updateLocationUI() {
        if (mMap==null){
            return
        }
        try {
            if (isLocationPermissionGranted){
                mMap.isMyLocationEnabled=true
                mMap.uiSettings.isMyLocationButtonEnabled=true
            }
            else{
                mMap.isMyLocationEnabled=false
                mMap.uiSettings.isMyLocationButtonEnabled=false
                mLastKnownLocation=null
                getLocationPermission()
            }
        }catch (e:SecurityException){
            Log.e("Map Activity Exception:", e.message)
        }
    }

    private fun getDeviceLocation(){
        try {
            if (isLocationPermissionGranted){
                var locationResult=mFusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful){
                        mLastKnownLocation=task.result
                        mMap.addMarker(MarkerOptions().position(LatLng(mLastKnownLocation!!.latitude,mLastKnownLocation!!.longitude)).title("Marker at Current Location")).setIcon(bitmapDescriptor)
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLastKnownLocation!!.latitude,
                                mLastKnownLocation!!.longitude),DEFAULT_ZOOM))
                    }
                    else{
                        Log.d("Exception: ","Current location is null. Using defaults.");
                        Log.e( "Exception: ", task.exception.toString());
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney,DEFAULT_ZOOM))
                        mMap.uiSettings.isMyLocationButtonEnabled=false
                    }
                })
            }
        }catch (e:SecurityException){
            Log.e("Exception:",e.message )
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

//    Camera Options

//     mMap.setOnCameraMoveCanceledListener
    override fun onCameraMoveCanceled() {

    }

//     mMap.setOnCameraMoveListener
     override fun onCameraMove() {

        var cameraPosition=mMap.cameraPosition
            if (USER_ZOOM!=cameraPosition.zoom){
                isZooming=true
//                mMap.uiSettings.isScrollGesturesEnabled=false
                if (isZooming==true){
                    mMap.addMarker(MarkerOptions().position(LatLng(mLastKnownLocation!!.latitude,mLastKnownLocation!!.longitude)).title("Marker at Current Location")).setIcon(bitmapDescriptor)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mLastKnownLocation!!.latitude,
                            mLastKnownLocation!!.longitude)))
                }
                Toast.makeText(this, "The camera is Zooming:" ,
                        Toast.LENGTH_SHORT).show();
            }
            USER_ZOOM=cameraPosition.zoom
    }

//    method of mMap.setOnCameraMoveStartedListener
    override fun onCameraMoveStarted(reason: Int) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            if (isZooming==false) {
                mMap.clear()

            }
            /*Toast.makeText(this, "The user gestured on the map.",
                    Toast.LENGTH_SHORT).show();*/

        }
    }

//    method of mMap.setOnCameraIdleListener
    override fun onCameraIdle() {

        var latLng= LatLng(mMap.cameraPosition.target.latitude,mMap.cameraPosition.target.longitude)

        mLastKnownLocation!!.longitude=latLng.longitude
        mLastKnownLocation!!.latitude=latLng.latitude

        mMap.addMarker(MarkerOptions().position(latLng).title("Marker at User Scrolled")).setIcon(bitmapDescriptor)
        isZooming=false
        mMap.uiSettings.isScrollGesturesEnabled=true
//        Toast.makeText(this, "The camera has stopped moving.",
//                Toast.LENGTH_SHORT).show();
    }




}
