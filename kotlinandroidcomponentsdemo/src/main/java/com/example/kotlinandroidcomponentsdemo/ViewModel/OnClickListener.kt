package com.example.kotlinandroidcomponentsdemo.ViewModel

interface OnClickListener {
    fun onClick(where : EnumClicks, data : Any, position : Int, boolean: Boolean )
}