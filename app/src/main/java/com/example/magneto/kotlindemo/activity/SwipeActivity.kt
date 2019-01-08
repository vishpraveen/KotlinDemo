package com.example.magneto.kotlindemo.activity

import android.animation.LayoutTransition
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.adapter.SwipeAdapter
import com.example.magneto.kotlindemo.inteface.EnumClicks
import com.example.magneto.kotlindemo.inteface.RecyclerOnClick
import com.example.magneto.kotlindemo.utility.DateTimeHelper
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*
import kotlin.collections.ArrayList

class SwipeActivity : AppCompatActivity(),RecyclerOnClick {

    private lateinit var cl_main: CoordinatorLayout
    private var recycler: RecyclerView?=null
    private var fab_add:FloatingActionButton?=null
    private var layoutManager:LinearLayoutManager?=null
    private var recyclerAdapter: SwipeAdapter?=null

    private var listener:RecyclerOnClick?=null
    private var dataList=ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_swipe)

        listener=this
        initUI()
        setOnClickListener()

        cl_main.layoutTransition.enableTransitionType(LayoutTransition.APPEARING )
    }

    private fun setOnClickListener() {
        fab_add?.setOnClickListener {
//            var i:Int=1
            val time:String?=DateTimeHelper.currentDate()
            dataList.add(time!!)
            val DateTimeHelper=DateTimeHelper()
            dataList.add(DateTimeHelper.currentTime()!!)
            recyclerAdapter?.notifyDataSetChanged()

        }
    }

    private fun initUI() {
        cl_main=findViewById(R.id.cl_main)
        fab_add=findViewById(R.id.fab_add)
        recycler=findViewById(R.id.recycler)
        layoutManager=LinearLayoutManager(this)
        recyclerAdapter= SwipeAdapter(this,dataList,listener)
        recycler?.layoutManager=layoutManager
        recycler?.adapter=recyclerAdapter

    }

    override fun onClick(where: EnumClicks, view: View, objects: Objects?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
