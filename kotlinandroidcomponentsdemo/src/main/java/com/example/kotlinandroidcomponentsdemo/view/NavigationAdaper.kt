package com.example.kotlinandroidcomponentsdemo.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.kotlinandroidcomponentsdemo.R
import com.example.kotlinandroidcomponentsdemo.interfaces.EnumClicks
import com.example.kotlinandroidcomponentsdemo.interfaces.OnClickListener
import com.example.kotlinandroidcomponentsdemo.model.NavigationModel

class NavigationAdaper(val context: Context?, val navList: ArrayList<NavigationModel>?, val listener: OnClickListener?) : RecyclerView.Adapter<NavigationAdaper.NavViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NavViewHolder {
        val view : View = LayoutInflater.from(context).inflate(R.layout.navlist,parent,false)
        return NavViewHolder(view)
    }

    override fun getItemCount(): Int {
        return navList!!.size
    }

    override fun onBindViewHolder(holder: NavViewHolder, position: Int) {
        Glide.with(context!!)
                .asBitmap()
                .load(navList!!.get(position).image)
                .into(holder.image)

        if (navList!!.get(position).isClicked) {
            holder.tvName.text = navList!!.get(position).name
            holder.tvName.setTextColor(ContextCompat.getColor(context,R.color.colorSecondary))
        }else{
            holder.tvName.text = navList!!.get(position).name
            holder.tvName.setTextColor(ContextCompat.getColor(context,R.color.textSecondary))
        }

        holder.navMain.setOnClickListener {
            listener?.onClick(EnumClicks.CELL,"",position,true)
        }
    }

    class NavViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        var navMain : ConstraintLayout = itemView.findViewById(R.id.navMain)
        var tvName : TextView = itemView.findViewById(R.id.tvName)
        var image : ImageView = itemView.findViewById(R.id.image)
    }
}
