package com.example.magneto.kotlindemo.utility

import java.text.SimpleDateFormat
import java.util.*

class DateTimeHelper {

    companion object {
        /*
       * currentDate can be called without creating the object of DateTimeHelper class
       * */
        fun currentDate():String?{
            val cal:Calendar= Calendar.getInstance()
//            val pattern:String="MM/dd/yyy"
            val sdf=SimpleDateFormat("MM/dd/yyy H:m:s",Locale.US)
            return sdf.format(cal.time)
        }
    }

    /*
    * currentTime can be called by creating the object of DateTimeHelper class
    * */
    fun currentTime(): String? {
        val cal=Calendar.getInstance()

        val sdf=SimpleDateFormat("H:m")
        return sdf.format(cal.time)
    }
}