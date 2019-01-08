package com.example.magneto.kotlindemo.adapter

//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.magneto.kotlindemo.activity.MainActivity
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.inteface.EnumClicks
import com.example.magneto.kotlindemo.inteface.RecycleClick
import kotlinx.android.synthetic.main.lay_list_adapter2.view.*

class List_Adapter2(val context: MainActivity, val data_list: ArrayList<String>, val click: RecycleClick) : RecyclerView.Adapter<List_Adapter2.MyViewHolder2>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MyViewHolder2 {
        val view=LayoutInflater.from(context).inflate(R.layout.lay_list_adapter2,p0,false)

        val animation= AnimationUtils.loadAnimation(context,R.anim.recycler_animate)
        animation.duration=500
        view.startAnimation(animation)

        return MyViewHolder2(view)
    }

    override fun getItemCount(): Int {
        return data_list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder2, position: Int) {
        holder.txt_name2.text=data_list.get(position)
        holder.txt_name2.setOnClickListener({ view ->  click.click(EnumClicks.CELL,view,position) })
    }

    class MyViewHolder2(view: View) : RecyclerView.ViewHolder(view){
        val txt_name2=view.txt_name2
    }
}
