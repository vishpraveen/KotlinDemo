package com.example.magneto.kotlindemo.services

import android.Manifest
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.core.content.PermissionChecker
import com.example.magneto.kotlindemo.R
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import android.R.attr.path
import android.app.NotificationChannel
import android.app.NotificationManager
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.android.gms.location.LocationResult
import android.location.Location
import android.os.Build
import androidx.localbroadcastmanager.content.LocalBroadcastManager


public class TrackerService : Service() {
//    private var TAG = TrackerService::class.simpleName
    private var TAG = "TrackerService"
    private var CHANNEL_ID : String = "Location"

    override fun onBind(intent: Intent): IBinder {
        null!!
    }

    override fun onCreate() {
        super.onCreate()
        buildNotification()
        loginToFirebase()
    }

    private fun loginToFirebase() {
        // Authenticate with Firebase, and request location updates
        var email = getString(R.string.firebase_email)
        var password = getString(R.string.firebase_password)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(
                email,
                password
        ).addOnCompleteListener { task ->
            if (task.isSuccessful){
                Log.e(TAG, getString(R.string.firebase_auth_succuss))
                requestLocationUpdates()
            }
            else{
                Log.e(TAG, getString(R.string.firebase_auth_failed))
            }
        }
    }

    private fun requestLocationUpdates() {
        var locationRequest : LocationRequest = LocationRequest()
        locationRequest.setInterval(10000)
        locationRequest.setFastestInterval(5000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        var client : FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        val path= getString(R.string.firebase_path)+"/"+getString(R.string.transport_id)
        var permission : Int = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission == PackageManager.PERMISSION_GRANTED){
            // Request location updates and when an update is
            // received, store the location in Firebase
            client.requestLocationUpdates(locationRequest,object : LocationCallback(){
                override fun onLocationResult(result: LocationResult?) {
                    super.onLocationResult(result)
                    val ref : DatabaseReference = FirebaseDatabase.getInstance().getReference(path)
                    val location : Location = result!!.lastLocation
                    if (location!=null){
                        Log.e(TAG, getString(R.string.location_updated)+location)
                        ref.setValue(location)
                    }
                }
            },null)
        }
    }

    private fun buildNotification() {
        var stop="stop"
        registerReceiver(stopReceiver, IntentFilter(stop))
        var broadcastIntent : PendingIntent = PendingIntent.getBroadcast(
                this,
                0,
                Intent(stop),
                PendingIntent.FLAG_UPDATE_CURRENT)

        // Create the persistent notification
        var builder :NotificationCompat.Builder = NotificationCompat.Builder(this,CHANNEL_ID)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(getString(R.string.notification_for_location))
                .setOngoing(true)
                .setContentIntent(broadcastIntent)
                .setSmallIcon(R.drawable.gps_android)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            val name = "Service"
            val descriptionText = "Tracking your location"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
                    .apply { description = descriptionText }
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)
        }

        startForeground(1,builder.build())
    }

    /*
    * Creating a BroadCast Receiver
    * */
    protected var stopReceiver : BroadcastReceiver = object :BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.e(TAG,getString(R.string.received_stop_broadcast))
            // Stop the service when the notification is tapped
            unregisterReceiver(this)
//            LocalBroadcastManager.getInstance(context!!).unregisterReceiver(this)
            stopSelf()
        }
    }


}
