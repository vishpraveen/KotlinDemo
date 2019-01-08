package com.example.magneto.kotlindemo.inteface

import android.view.View
import java.util.*

interface RecyclerOnClick {
    fun onClick(where:EnumClicks, view:View, objects: Objects?, position:Int)
}