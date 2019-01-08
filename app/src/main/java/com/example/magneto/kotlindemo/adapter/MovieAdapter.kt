package com.example.magneto.kotlindemo.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Rect
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.bean.MovieBean
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.layout_movie.view.*


class MovieAdapter(val mContext: Context,val movieList: ArrayList<MovieBean>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view:View=LayoutInflater.from(mContext).inflate(R.layout.layout_movie,parent,false)
        return MovieViewHolder(view)
    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        Glide.with(mContext)
                .load(movieList[position].Poster)
                .into(holder.ivMovie)

        holder.tvMovieName.setText(movieList[position].Title)
        holder.tvYear.setText(movieList[position].Year)
        holder.tvRating.setText(movieList[position].imdbRating)
        holder.tvBoxOffice.setText(movieList[position].BoxOffice)
        holder.tvDirector.setText(movieList[position].Director)
        holder.tvActor.setText(movieList[position].Actors)
        holder.tvPlot.setText(movieList[position].Plot)
        holder.tvProduction.setText(movieList[position].Production)
        holder.tvAward.setText(movieList[position].Awards)


        val genre= movieList[position].Genre.split(",")
//        Log.e("Genre",genre.size.toString())
        for (index in genre.indices){
            val chip=Chip(holder.chipGroup.context)
            chip.text=genre[index]
            chip.chipBackgroundColor= ColorStateList.valueOf(ContextCompat.getColor(mContext,R.color.red))

//            chip.setTextColor(R.color.white)
            holder.chipGroup.addView(chip)
//            Log.e("Genre",genre[index])
        }
//        holder.tvYear.setText(movieList[position].Year)
//        holder.tvReleased.setText(movieList[position].Released)
//        holder.tvRuntime.setText(movieList[position].Runtime)

    }

    class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val ivMovie=view.ivMovie
        val tvMovieName=view.tvMovieName
        val tvYear=view.tvYear
//        val tvReleased=view.tvReleased
//        val tvRuntime=view.tvRuntime
//        val tvLanguage=view.tvLanguage
        val tvRating=view.tvRating
        val tvBoxOffice=view.tvBoxOffice
        val chipGroup=view.chipGroup
        val tvDirector=view.tvDirector
        val tvActor=view.tvActor
        val tvPlot=view.tvPlot
        val tvProduction=view.tvProduction
        val tvAward=view.tvAward
    }
}
