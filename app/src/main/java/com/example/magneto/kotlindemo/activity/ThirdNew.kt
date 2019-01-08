package com.example.magneto.kotlindemo.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.adapter.NavigationAdapter
import com.example.magneto.kotlindemo.bean.NavModel
import com.example.magneto.kotlindemo.inteface.EnumClicks
import com.example.magneto.kotlindemo.inteface.RecyclerOnClick
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.*
import kotlin.collections.ArrayList

class ThirdNew : AppCompatActivity(),RecyclerOnClick  {

    private var drawerLayout:DrawerLayout?=null
    var bar:BottomAppBar?=null
    private var recyclerNavMenu:RecyclerView?=null
    var layoutManager:LinearLayoutManager?=null
    private var navAdapter:NavigationAdapter?=null
    private var mContext:Context?=null
    private var listener:RecyclerOnClick?=null

    private var navList=ArrayList<NavModel>()
    var fab:FloatingActionButton?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_new)

        mContext=this
        listener=this

        initUI()
        onClickListener()

        animation_view.playAnimation()
        animation_view1.playAnimation()

    }

    private fun onClickListener() {
        bar?.setNavigationOnClickListener { drawerLayout?.openDrawer(GravityCompat.START) }
        fab?.setOnClickListener {
            val intent=Intent(mContext,SplashActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initUI() {
        bar=findViewById(R.id.bar)
        drawerLayout=findViewById(R.id.drawer_layout)
        recyclerNavMenu=findViewById(R.id.recycler_nav_menu)
        layoutManager= LinearLayoutManager(mContext)

        recyclerNavMenu?.layoutManager=layoutManager
        navAdapter=NavigationAdapter(mContext,navList,listener)
        recyclerNavMenu?.adapter= navAdapter

        fab=findViewById(R.id.fab)
        addNavMenuItems()
    }

    private fun addNavMenuItems() {
//        navList= ArrayList<NavModel>()
        val model1= NavModel()
        model1.image=R.drawable.ic_home
        model1.name=getString(R.string.nav_home)

        val model2= NavModel()
        model2.image=R.drawable.ic_favorite
        model2.name=getString(R.string.nav_profile)

        val model3= NavModel()
        model3.image=R.drawable.ic_contact_phone
        model3.name=getString(R.string.nav_contact)

        val model4= NavModel()
        model4.image=R.drawable.ic_contact_mail
        model4.name=getString(R.string.nav_movie)

        val model5= NavModel()
        model5.image=R.drawable.ic_exit_to_app
        model5.name=getString(R.string.nav_logout)

        navList.add(model1)
        navList.add(model2)
        navList.add(model3)
        navList.add(model4)
        navList.add(model5)

        navAdapter!!.notifyDataSetChanged()

    }

    override fun onClick(where: EnumClicks, view: View, objects: Objects?, position: Int) {
        val model= navList[position]
//        Toast.makeText(mContext,"Clicked on: "+model.name+" \n Position is: "+position,Toast.LENGTH_SHORT).show()

        if (position==0){
            drawerLayout?.closeDrawer(GravityCompat.START)
            val intent=Intent(mContext,MainActivity::class.java)
            startActivity(intent)
        }
        if (position==1){
            drawerLayout?.closeDrawer(GravityCompat.START)
            val intent=Intent(mContext,SwipeActivity::class.java)
            startActivity(intent)
        }
        if (position==2){
            drawerLayout?.closeDrawer(GravityCompat.START)
            val intent=Intent(mContext,AnimationActivity::class.java)
            startActivity(intent)
        }
        if (position==3){
            drawerLayout?.closeDrawer(GravityCompat.START)
            val intent=Intent(mContext,MovieDetails::class.java)
            startActivity(intent)
        }

    }
}
