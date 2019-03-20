package com.example.magneto.kotlindemo.activity

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.FragmentActivity
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.services.TrackerService
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.google.android.gms.dynamic.IObjectWrapper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*

import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
//import com.google.android.libraries.places.api.Places
//import com.google.android.libraries.places.api.model.Place
//import com.google.android.libraries.places.widget.AutocompleteSupportFragment
//import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*
import kotlin.collections.ArrayList

class MapsActivity : FragmentActivity(), OnMapReadyCallback, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveListener, GoogleMap.OnCameraMoveCanceledListener {


    private lateinit var mMap: GoogleMap
    private lateinit var mGoogleApiClient:GoogleApiClient
//    private lateinit var mGeoDataClient:GeoDataClient
//    private lateinit var mPlaceDetectionClient:PlaceDetectionClient
    private lateinit var mFusedLocationProviderClient:FusedLocationProviderClient
    private var mLastKnownLocation:Location?=null
    private lateinit var myLocation : CardView
    private lateinit var mContent:Context

    private var isLocationPermissionGranted=false
    private var PERMISSION_REQUEST_LOCATION=10
    private var DEFAULT_ZOOM=18.0f
    private var USER_ZOOM=DEFAULT_ZOOM
    private var isZooming=false

    private val sydney = LatLng(-34.0, 151.0)

    var bitmapDescriptor:BitmapDescriptor? =null

    private var iv_gps:ImageView?=null

    private var mMarkers : HashMap<String, Marker> = hashMapOf()

    private val TAG = "MapsActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map_new)

        mContent=this
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getLocationPermission()

//        mGeoDataClient=Places.getGeoDataClient(this)

//        mPlaceDetectionClient=Places.getPlaceDetectionClient(this)

        mFusedLocationProviderClient=LocationServices.getFusedLocationProviderClient(this)

        /*mGoogleApiClient=GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this,this)
                .build()*/

        Toast.makeText(this, "On Map Activity", Toast.LENGTH_LONG).show()

//        Check GPS is enabled
        var locationManager: LocationManager  = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, getString(R.string.enable_location), Toast.LENGTH_SHORT).show()
//            finish()
        }

// Check location permission is granted - if it is, start
// the service, otherwise request the permission
        var permission : Int= ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
        if (permission == PackageManager.PERMISSION_GRANTED){
            startTrackerService()
        }
        else{
            ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    PERMISSION_REQUEST_LOCATION)
        }

//        iv_gps=findViewById(R.id.iv_gps)
//        bitmapDescriptor=BitmapDescriptorFactory.fromResource(R.drawable.ic_gps_individual)
//        animation_view.playAnimation()
        //        current Location button
        myLocation=findViewById(R.id.myLocation)
        myLocation.setOnClickListener {
            getMyLocation()
        }

//        Adding PlacesAutoComplete
        /**
         * Initialize Places. For simplicity, the API key is hard-coded. In a production
         * environment we recommend using a secure mechanism to manage API keys.
         */
        /*if (!Places.isInitialized()){
            Places.initialize(applicationContext,"AIzaSyC3L3Rjpfn77IxCd753mOUwccaSlK3kEw4")
        }
        // Initialize the AutocompleteSupportFragment.
        val autoCompleteFragment : AutocompleteSupportFragment = supportFragmentManager.findFragmentById(R.id.autoComplete) as AutocompleteSupportFragment
        autoCompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME))
        autoCompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onPlaceSelected(place : Place) {
                Toast.makeText(mContent,place.name+", "+place.id,Toast.LENGTH_LONG).show()
            }

            override fun onError(satus : Status) {
                Toast.makeText(mContent,place.name+", "+place.id,Toast.LENGTH_LONG).show()
            }

        })*/

    }

    private fun getMyLocation() {
        var location : Location = mMap.myLocation
        var latlng : LatLng= LatLng(location.latitude,location.longitude)

        var cameraPosition : CameraPosition = CameraPosition.fromLatLngZoom(latlng,mMap.cameraPosition.zoom)
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun startTrackerService() {
        startService(Intent(this,TrackerService::class.java))
//        finish()
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
                    updateLocationUI()
                }
                else{
                    Toast.makeText(this, getString(R.string.permission_not_granted), Toast.LENGTH_SHORT).show()
                }
            }
        }

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
        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style))
        mMap.uiSettings.isMyLocationButtonEnabled=false
        mMap.uiSettings.isRotateGesturesEnabled=false
        mMap.uiSettings.isScrollGesturesEnabled=true
        mMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom=false

        updateLocationUI()

        getDeviceLocation()

//        Logging to firebase
        loginTOFirebase()

        mMap.setOnCameraIdleListener(this)
        mMap.setOnCameraMoveStartedListener(this);
        mMap.setOnCameraMoveListener(this);
        mMap.setOnCameraMoveCanceledListener(this);

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDragEnd(marker : Marker?) {
                var position : LatLng =marker!!.position
                getLocationDetails(position)
            }

            override fun onMarkerDragStart(marker : Marker?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onMarkerDrag(marker : Marker?) {
//                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        // Add a marker in Sydney and move the camera


//        val currentLocation = LatLng(mLastKnownLocation!!.latitude,mLastKnownLocation!!.longitude)
//        Log.e("Loaction",sydney.toString())
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker at Sydney")).setIcon(bitmapDescriptor)

//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
    }

    private fun getLocationDetails(position: LatLng) {
        var geocoder = Geocoder(mContent, Locale.getDefault())

        var addresses : ArrayList<Address>
        addresses = geocoder.getFromLocation(position.latitude,position.longitude,1) as ArrayList<Address>
        var address =addresses[0].getAddressLine(0)
        var city = addresses[0].locality

        Toast.makeText(mContent, "New Address: $address $city", Toast.LENGTH_SHORT).show()
    }

    private fun loginTOFirebase() {
        var email=getString(R.string.firebase_email)
        var password=getString(R.string.firebase_password)
        // Authenticate with Firebase and subscribe to updates
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email,
                password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful){
                suscribeToUpdates()
                Log.e(TAG, getString(R.string.firebase_auth_succuss))
            }
            else{
                Log.e(TAG, getString(R.string.firebase_auth_failed))
            }
        }
    }

    private fun suscribeToUpdates() {
        var ref : DatabaseReference = FirebaseDatabase.getInstance().getReference(getString(R.string.firebase_path))
        ref.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG,getString(R.string.failed_to_read_value)+error.toException())
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                //Keep it blank
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                setMarker(dataSnapshot)
            }

            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                setMarker(dataSnapshot)
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                //Keep it blank
            }

        })
    }

    private fun setMarker(dataSnapshot: DataSnapshot) {
        // When a location update is received, put or update
        // its value in mMarkers, which contains all the markers
        // for locations received, so that we can build the
        // boundaries required to show them all on the map at once
        var key : String? = dataSnapshot.key
        var value : HashMap<String, Any> = dataSnapshot.value as HashMap<String, Any>
        var lat : Double = value.get("latitude").toString().toDouble()
        var lng : Double = value.get("longitude").toString().toDouble()
        var location : LatLng= LatLng(lat,lng)
        if (!mMarkers.containsKey(key)){
            mMarkers.put(key!!, mMap.addMarker(MarkerOptions().title(key).position(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.car))))
        }
        else{
            mMarkers.get(key)?.position=location
        }
        var builder : LatLngBounds.Builder = LatLngBounds.Builder()
        for (marker in mMarkers.values) {
            builder.include(marker.position)
        }
//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(),300))
    }

    private fun updateLocationUI() {
        if (mMap==null){
            return
        }
        try {
            if (isLocationPermissionGranted){
                mMap.isMyLocationEnabled=true
                mMap.uiSettings.isMyLocationButtonEnabled=false
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
                        if (task.result!=null) {
                            mLastKnownLocation = task.result
                            mMap.addMarker(MarkerOptions().position(LatLng(mLastKnownLocation!!.latitude, mLastKnownLocation!!.longitude))
                                    .title("Marker at Current Location")
                                    .draggable(true))
                                    .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(mLastKnownLocation!!.latitude,
                                    mLastKnownLocation!!.longitude), DEFAULT_ZOOM))
                        }
                        else{
                            Log.e(TAG,getString(R.string.task_is_null))
                            getDeviceLocation()
                        }
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

       /* var cameraPosition=mMap.cameraPosition
            if (USER_ZOOM!=cameraPosition.zoom){
                isZooming=true
//                mMap.uiSettings.isScrollGesturesEnabled=false
                if (isZooming==true){
//                    mMap.addMarker(MarkerOptions().position(LatLng(mLastKnownLocation!!.latitude,mLastKnownLocation!!.longitude)).title("Marker at Current Location")).setIcon(bitmapDescriptor)
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(LatLng(mLastKnownLocation!!.latitude,
                            mLastKnownLocation!!.longitude)))
                }
                Toast.makeText(this, "The camera is Zooming:" ,
                        Toast.LENGTH_SHORT).show();
            }
            USER_ZOOM=cameraPosition.zoom*/
    }

//    method of mMap.setOnCameraMoveStartedListener
    override fun onCameraMoveStarted(reason: Int) {
        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
            if (isZooming==false) {
//                mMap.clear()

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

//        mMap.addMarker(MarkerOptions().position(latLng).title("Marker at User Scrolled")).setIcon(bitmapDescriptor)
        isZooming=false
        mMap.uiSettings.isScrollGesturesEnabled=true
//        mMap.clear()
        suscribeToUpdates()
    }




}
