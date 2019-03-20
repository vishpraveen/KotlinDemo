package com.example.magneto.kotlindemo.utility

import android.util.Log

class Utility {
    private val TAG : String? =Utility::class.simpleName

    init {
        Log.e(TAG,"In Use")
    }

    companion object {
        fun getDirectionsApiUrl(origin : String, destination : String) : String{
//            https://maps.googleapis.com/maps/api/directions/json?
// origin=Toronto&destination=Montreal&avoid=highways&mode=bicycling&key=AIzaSyAgQnrGOOtZSSL3CtqeiBTm9t8g5kT4GUA
            var origin = "origin=$origin"
            var destination = "destination=$destination"
            var avoid="avoid=highways"
            var mode="mode=bicycling"
            var key="key=AIzaSyAgQnrGOOtZSSL3CtqeiBTm9t8g5kT4GUA"
            var directionsUrl = "https://maps.googleapis.com/maps/api/directions/json?$origin&$destination&$avoid&$mode&$key"
            return  directionsUrl
        }
    }
}