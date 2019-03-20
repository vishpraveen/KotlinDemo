package com.example.kotlinandroidcomponentsdemo.interfaces

interface OnClickListener {
    fun onClick(where : EnumClicks, data : Any, position : Int, boolean: Boolean )
}