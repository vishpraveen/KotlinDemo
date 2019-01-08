package com.example.magneto.kotlindemo.inteface

import android.view.View

interface RecycleClick {
    fun click(where:EnumClicks, view:View, position:Int)
}