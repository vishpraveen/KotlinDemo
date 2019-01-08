package com.example.magneto.kotlindemo.adapter

import android.content.Context
//import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.magneto.kotlindemo.R
import kotlinx.android.synthetic.main.sheet_adapter_view.view.*

class MyBottomSheetAdapter(var context: Context, var data_list: ArrayList<String>) : RecyclerView.Adapter<MyBottomSheetAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view:View = LayoutInflater.from(context).inflate(R.layout.sheet_adapter_view,parent,false)

        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data_list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.names.text=data_list.get(position)
    }

    class MyViewHolder( itemView: View): RecyclerView.ViewHolder(itemView){
        var names=itemView.findViewById<TextView>(R.id.tv_text);
    }

}
