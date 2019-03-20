package com.example.magneto.kotlindemo.activity

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.services.TrackerService
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Status
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class TrackingActivity : FragmentActivity(), OnMapReadyCallback,
        GoogleMap.OnCameraIdleListener,
        GoogleMap.OnCameraMoveStartedListener,
        GoogleMap.OnCameraMoveListener,
        GoogleMap.OnCameraMoveCanceledListener {

    private lateinit var mMap: GoogleMap
    private lateinit var mContent: Context
    private var isLocationPermissionGranted=false
    private var PERMISSION_REQUEST_LOCATION=10
    private var DEFAULT_ZOOM=18.0f
    private lateinit var mLastKnownLocation: Location
    private lateinit var currentLocation: LatLng
    private lateinit var sourceLocation: LatLng
    private lateinit var destinationLocation: LatLng
    private lateinit var mFusedLocationProviderClient: FusedLocationProviderClient
    private val sydney = LatLng(-34.0, 151.0)
    private var mMarkers : HashMap<String, Marker> = hashMapOf()
    private lateinit var myLocation : CardView
    private lateinit var route : CardView
    // Define a Place ID.
    private var placeId : String = "10"
    private lateinit var placesClient: PlacesClient
    private lateinit var autocompleteFragment : AutocompleteSupportFragment
//    private lateinit var autocomplete_fragment_destination : AutocompleteSupportFragment
    private val TAG = "TrackingActivity"
    private val POLYLINE_STROKE_WIDTH_PX : Float= 12.0F

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tracking)

        mContent=this
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
                .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        getLocationPermission()
        mFusedLocationProviderClient= LocationServices.getFusedLocationProviderClient(this)

        //        Check GPS is enabled
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
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

        //        current Location button
        myLocation=findViewById(R.id.myLocation)
        route=findViewById(R.id.route)
        myLocation.setOnClickListener {
            getMyLocation()
        }
        route.setOnClickListener {
            Toast.makeText(mContent, "This functionality is billable", Toast.LENGTH_SHORT).show()
        }
        /*
        * Adding Google AutoComplete
        * */
        // Initialize the AutocompleteSupportFragment.
        autocompleteFragment= supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment
//        autocomplete_fragment_destination= supportFragmentManager.findFragmentById(R.id.autocomplete_fragment_destination) as AutocompleteSupportFragment
        // autocompleteFragment set hint
        autocompleteFragment.setHint(getString(R.string.search_source))
//        autocomplete_fragment_destination.setHint(getString(R.string.search_destination))
        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG))
//        autocomplete_fragment_destination.setPlaceFields(Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG))
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onPlaceSelected(place: Place) {
            // TODO: Get info about the selected place.
                if (place.latLng!=null) {
                    sourceLocation = place.latLng!!
                    Toast.makeText(mContent, place.name, Toast.LENGTH_SHORT).show()
//                    mMap.clear()
                    mMap.addMarker(MarkerOptions().position(LatLng(sourceLocation!!.latitude, sourceLocation!!.longitude))
                            .title("Marker at Current Location")
                            .draggable(true))
                            .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
//                    var cameraPosition : CameraPosition = CameraPosition.fromLatLngZoom(sourceLocation,mMap.cameraPosition.zoom)
//                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
//                    drawPolyLine()
                }
                else{
                    Toast.makeText(mContent, getString(R.string.location_not_found), Toast.LENGTH_SHORT).show()
                }


            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.e(TAG, "AutoCompleteFragment: " + status.statusMessage)
            }
        })
       /* autocomplete_fragment_destination.setOnPlaceSelectedListener(object : PlaceSelectionListener{
            override fun onPlaceSelected(place: Place) {
            // TODO: Get info about the selected place.
                if (place.latLng!=null){
                    destinationLocation = place.latLng!!
                    Toast.makeText(mContent, place.name, Toast.LENGTH_SHORT).show()
                    mMap.addMarker(MarkerOptions().position(LatLng(destinationLocation!!.latitude, destinationLocation!!.longitude))
                            .title("Marker at Current Location")
                            .draggable(true))
                            .setIcon(BitmapDescriptorFactory.fromResource(R.drawable.ic_marker))
                    var cameraPosition : CameraPosition = CameraPosition.fromLatLngZoom(destinationLocation,mMap.cameraPosition.zoom)
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
                }else{
                    Toast.makeText(mContent, getString(R.string.location_not_found), Toast.LENGTH_SHORT).show()
                }
            }

            override fun onError(status: Status) {
                // TODO: Handle the error.
                Log.e(TAG, "AutoCompleteFragment: " + status.statusMessage)
            }
        })*/
        // Initialize Places.
        Places.initialize(getApplicationContext(), "AIzaSyAgQnrGOOtZSSL3CtqeiBTm9t8g5kT4GUA")
//        Places.initialize(getApplicationContext(), "AIzaSyC3L3Rjpfn77IxCd753mOUwccaSlK3kEw4")
        // Create a new Places client instance.
        placesClient = Places.createClient(this);
        // Specify the fields to return.
        var placeField : List<Place.Field> = Arrays.asList(Place.Field.ID,Place.Field.NAME,Place.Field.LAT_LNG)
        // Construct a request object, passing the place ID and fields array.
        var request : FetchPlaceRequest = FetchPlaceRequest.builder(placeId,placeField)
                .build()
        // Add a listener to handle the response.
        placesClient.fetchPlace(request).addOnSuccessListener { fetchPlaceResponse ->
            var place =fetchPlaceResponse.place
            Toast.makeText(mContent, place.name, Toast.LENGTH_SHORT).show();
        }.addOnFailureListener { exception ->
            if (exception is ApiException){
                var apiException = exception
                var statusCode : Int = apiException.statusCode
                // Handle error with given status code.
                Log.e(TAG, "Place not found: " + exception.message+", ErrorCode: "+statusCode);
            }
        }
    }

    private fun drawPolyLine() {
        // Add polylines to the map. This section shows just
        // a single polyline. Read the rest of the tutorial to learn more.
        if (currentLocation!=null && sourceLocation!=null) {
            var polyline = mMap.addPolyline(PolylineOptions().add(LatLng(18.5957016,73.753658),
                    LatLng(18.5986239,73.7435984)).clickable(true))

//            var builder : LatLngBounds.Builder = LatLngBounds.Builder()
//            for (marker in mMarkers.values) {
//                builder.include(marker.position)
//            }
        }
    }


    private fun getMyLocation() {
//        mMap.clear()
        var location : Location = mMap.myLocation
        var latlng : LatLng= LatLng(location.latitude,location.longitude)
        currentLocation = latlng
        var cameraPosition : CameraPosition = CameraPosition.fromLatLngZoom(latlng,mMap.cameraPosition.zoom)
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))

//        getDeviceLocation()
//        loginTOFirebase()
    }

    private fun startTrackerService() {
        startService(Intent(this, TrackerService::class.java))
    }

    private fun getLocationPermission() {
        if (ContextCompat.checkSelfPermission(mContent,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED){

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

    private fun updateLocationUI() {
        if (mMap==null){
            return
        }
        try {
            if (isLocationPermissionGranted){
                mMap.isMyLocationEnabled=true
                mMap.uiSettings.isMyLocationButtonEnabled=false
//                onCameraIdle()
                getDeviceLocation()
            }
            else{
                mMap.isMyLocationEnabled=false
                mMap.uiSettings.isMyLocationButtonEnabled=false
//                mLastKnownLocation=null
                getLocationPermission()
            }
        }catch (e:SecurityException){
            Log.e("Map Activity Exception:", e.message)
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
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this,R.raw.map_style))
        mMap.uiSettings.isMyLocationButtonEnabled=false
        mMap.uiSettings.isRotateGesturesEnabled=false
        mMap.uiSettings.isScrollGesturesEnabled=true
        mMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom=false

        updateLocationUI()

//        getDeviceLocation()

//        Logging to firebase
        loginTOFirebase()

        mMap.setOnCameraIdleListener(this)
        mMap.setOnCameraMoveStartedListener(this)
        mMap.setOnCameraMoveListener(this)
        mMap.setOnCameraMoveCanceledListener(this)

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener{
            override fun onMarkerDragEnd(marker : Marker?) {
                var position : LatLng =marker!!.position
                currentLocation = position
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

    override fun onCameraMoveStarted(p0: Int) {

    }

    override fun onCameraMove() {

    }

    override fun onCameraMoveCanceled() {

    }

    //    method of mMap.setOnCameraIdleListener
    override fun onCameraIdle() {
        if (isLocationPermissionGranted) {
            var latLng = LatLng(mMap.cameraPosition.target.latitude, mMap.cameraPosition.target.longitude)

            mLastKnownLocation!!.longitude = latLng.longitude
            mLastKnownLocation!!.latitude = latLng.latitude

//        mMap.addMarker(MarkerOptions().position(latLng).title("Marker at User Scrolled")).setIcon(bitmapDescriptor)
            mMap.uiSettings.isScrollGesturesEnabled = true
//        mMap.clear()
            suscribeToUpdates()
        }
    }


    private fun getDeviceLocation(){
        try {
            if (isLocationPermissionGranted){
                var locationResult=mFusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this, OnCompleteListener { task ->
                    if (task.isSuccessful){
                        if (task.result!=null) {
                            mLastKnownLocation = task.result!!
                            currentLocation = LatLng(mLastKnownLocation.altitude,mLastKnownLocation.longitude)
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
        ref.addChildEventListener(object : ChildEventListener {
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
}
