package com.example.magneto.kotlindemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.inteface.RecyclerOnClick

class SwipeAdapter(var mContext: Context, var dataList: ArrayList<String>,var listener: RecyclerOnClick? ):RecyclerView.Adapter<SwipeAdapter.SwipeViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SwipeViewHolder {
        var view:View=LayoutInflater.from(mContext).inflate(R.layout.swipe_layout,parent,false)
        return SwipeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: SwipeViewHolder, position: Int) {
        holder.tv_text.text=dataList.get(position)
    }


    class SwipeViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        var tv_text:TextView=itemView.findViewById(R.id.tv_text)
    }
}
