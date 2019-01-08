package com.example.magneto.kotlindemo.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.magneto.kotlindemo.R
import com.example.magneto.kotlindemo.adapter.MovieAdapter
import com.example.magneto.kotlindemo.bean.MovieBean
import com.example.magneto.kotlindemo.retrofit.APIService
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerFrameLayout
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovieDetails : AppCompatActivity() {

    private lateinit var edtMovie: EditText
    private lateinit var btnSearch: Button
    private lateinit var recyclerList: RecyclerView
    private lateinit var layoutManager: LinearLayoutManager
    private lateinit var movieAdapter:MovieAdapter
    private lateinit var progress:ProgressBar
//    private lateinit var shimmer: ShimmerFrameLayout
    private var movieList: ArrayList<MovieBean> = ArrayList()
    private val key:String="b4e9e1c5"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_details)

        initUI()
        onClickListener()

    }

    private fun onClickListener() {
        btnSearch.setOnClickListener {
            val name:String?=edtMovie.text.toString()
//            shimmer.startShimmer()

            searchMovie(name)
        }
    }

    private fun searchMovie(name: String?) {
        APIService.getBaseUrl().getMovieDetails(name!!,key)
                .enqueue(object :Callback<MovieBean>{
                    override fun onFailure(call: Call<MovieBean>, t: Throwable) {
//                        if (shimmer.isShimmerStarted) {
//                            shimmer.stopShimmer()
//                        }

                        Log.e("error",t.message)
                        msg(t.message)
                    }

                    override fun onResponse(call: Call<MovieBean>, response: Response<MovieBean>) {
                        if (response.isSuccessful){
                            if(response.body()!!.Response.equals("True")) {
                                if (movieList != null) {
                                    movieList.clear()
                                }
                                val movie:MovieBean =response.body()!!
//                                movie.Poster = response.body()!!.Poster
//                                movie.Title = response.body()!!.Title
//                                movie.Year = response.body()!!.Year

                                movieList.add(movie)
                                movieAdapter.notifyDataSetChanged()
//                                if (shimmer.isShimmerStarted) {
//                                    shimmer.stopShimmer()
//                                }
                            }
                            else{
//                                if (shimmer.isShimmerStarted) {
//                                    shimmer.stopShimmer()
//                                }
                                msg(response.body()!!.Error)
                            }
                        }
                        else{
//                            if (shimmer.isShimmerStarted) {
//                                shimmer.stopShimmer()
//                            }
                            msg("not found")
                        }

                    }
                })
    }

    private fun initUI() {
        edtMovie=findViewById(R.id.edtMovie)
        btnSearch=findViewById(R.id.btnSearch)
        recyclerList=findViewById(R.id.recyclerList)
        
//        shimmer=findViewById(R.id.shimmer)
        layoutManager= LinearLayoutManager(this)
        recyclerList.layoutManager=layoutManager
        movieAdapter= MovieAdapter(this,movieList)
        recyclerList.adapter=movieAdapter
    }

    private fun msg(message:String?){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
    }
}


