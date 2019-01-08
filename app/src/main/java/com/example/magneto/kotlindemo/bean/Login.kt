package com.example.magneto.kotlindemo.bean

import com.google.gson.annotations.SerializedName

/*
data class Login(
        @SerializedName("code")
        val code: Int,
        @SerializedName("message")
        val message: String,
        @SerializedName("newFlag")
        val newFlag: Int,
        @SerializedName("data")
        val data: Data1) {

    data class Data1(
            @SerializedName("userID")
            val userID: Int,
            @SerializedName("name")
            val name: String,
            @SerializedName("email")
            val email: String,
            @SerializedName("event")
            val event: ArrayList<Event>
    ) {
        data class Event(
                @SerializedName("id")
                val id: String,
                @SerializedName("eventType")
                val eventType: String
        )
    }


}*/

data class Login(
        @SerializedName("code")
        var code: Int = 0,
        @SerializedName("message")
        var message: String? = null,
        @SerializedName("newFlag")
        var newFlag: Int = 0,
        @SerializedName("data")
        var data: Data1? = null) {


    data class Data1(@SerializedName("userID")
                     var userID: Int = 0,
                     @SerializedName("name")
                     var name: String? = null,
                     @SerializedName("email")
                     var email: String? = null,
                     @SerializedName("image")
                     var image: String? = null,
                     @SerializedName("address")
                     var address: String? = null,
                     @SerializedName("mobileNumber")
                     var mobileNumber: String? = null,
                     @SerializedName("addressParts")
                     var addressParts: AddressParts? = null,
                     @SerializedName("event")
                     var event: ArrayList<Event>? = null,
                     @SerializedName("notification")
                     var notification: String? = null,
                     @SerializedName("dob")
                     var dob: String? = null,
                     @SerializedName("tokenID")
                     var tokenID: String? = null) {


        data class AddressParts(@SerializedName("addressLine1")
                                var addressLine1: String? = null,
                                @SerializedName("addressLine2")
                                var addressLine2: String? = null,
                                @SerializedName("city")
                                var city: String? = null,
                                @SerializedName("postalcode")
                                var postalcode: String? = null,
                                @SerializedName("state")
                                var state: String? = null,
                                @SerializedName("country")
                                var country: String? = null)

        data class Event(@SerializedName("id")
                         var id: String? = null,
                         @SerializedName("eventType")
                         var eventType: String? = null,
                         @SerializedName("eventDate")
                         var eventDate: String? = null)
    }
}

