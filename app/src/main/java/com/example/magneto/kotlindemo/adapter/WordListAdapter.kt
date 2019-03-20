package com.example.magneto.kotlindemo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.adapter.WordListAdapter.WordViewHolder
import com.example.magneto.kotlindemo.bean.Word

class WordListAdapter(val mContext: Context?) : RecyclerView.Adapter<WordViewHolder>(){
    private var words = emptyList<Word>() // Cached copy of words

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordViewHolder {
        val view : View =LayoutInflater.from(mContext).inflate(R.layout.adapter_list,parent,false)
        return WordViewHolder(view)
    }

    override fun getItemCount(): Int {
        return words.size
    }

    override fun onBindViewHolder(holder: WordViewHolder, position: Int) {
        holder.tvWord.text = words[position].word
    }

    internal fun setWords(words : List<Word>){
        this.words=words
        notifyDataSetChanged()
    }

    inner class WordViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView) {
        val tvWord : TextView= itemView.findViewById(R.id.tvWord)
    }

}
