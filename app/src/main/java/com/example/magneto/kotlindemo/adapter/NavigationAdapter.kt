package com.example.magneto.kotlindemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.bean.NavModel
import com.example.magneto.kotlindemo.inteface.EnumClicks
import com.example.magneto.kotlindemo.inteface.RecyclerOnClick

class NavigationAdapter(var mContext: Context?,var navList: ArrayList<NavModel>?,var listener: RecyclerOnClick?) : RecyclerView.Adapter<NavigationAdapter.NavigationViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavigationViewHolder {
        val view:View=LayoutInflater.from(mContext).inflate(R.layout.nav_list,parent,false)
        return NavigationViewHolder(view)
    }

    override fun getItemCount(): Int {
        return navList!!.size
    }

    override fun onBindViewHolder(holder: NavigationViewHolder, position: Int) {
        holder.iv_nav.setImageResource(navList!!.get(position).image)
        holder.tv_nav.text=navList!!.get(position).name

        holder.nav_list_main.setOnClickListener {
            listener!!.onClick(EnumClicks.CELL,holder.nav_list_main,null,position)
        }
    }


    class NavigationViewHolder(view:View): RecyclerView.ViewHolder(view){
        var nav_list_main:ConstraintLayout=view.findViewById(R.id.nav_list_main)
        var iv_nav:ImageView=view.findViewById(R.id.iv_nav)
        var tv_nav:TextView=view.findViewById(R.id.tv_nav)
    }
}
