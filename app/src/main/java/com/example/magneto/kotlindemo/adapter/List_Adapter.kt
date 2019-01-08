package com.example.magneto.kotlindemo.adapter

//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.magneto.kotlindemo.activity.MainActivity
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.inteface.EnumClicks
import com.example.magneto.kotlindemo.inteface.RecycleClick
import kotlinx.android.synthetic.main.lay_list_adapter.view.*

class List_Adapter(val context: MainActivity, val data_list: ArrayList<String>, val click: RecycleClick) : RecyclerView.Adapter<List_Adapter.NameViewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): NameViewHolder {

        val view=LayoutInflater.from(context).inflate(R.layout.lay_list_adapter,p0,false)

        val animation= AnimationUtils.loadAnimation(context,R.anim.mtrl_bottom_sheet_slide_out)
        animation.duration=500
        view.startAnimation(animation)
        return NameViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data_list.size
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        holder.txt_name.text=data_list.get(position)
        holder.rl.setOnClickListener({ view: View ->  click.click(EnumClicks.CELL,view,position)})
    }

    class NameViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val rl=view.rl
        val txt_name=view.txt_name
        val shimmer=view.shimmer
    }
}
