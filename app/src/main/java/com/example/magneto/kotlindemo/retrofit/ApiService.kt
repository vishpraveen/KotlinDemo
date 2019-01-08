package com.example.magneto.kotlindemo.retrofit


import com.example.magneto.kotlindemo.bean.Login
import com.example.magneto.kotlindemo.bean.MovieBean

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        var BASE_URL = "http://sale24by7.com/metreat/backend/web/api/"
        //Singleton Class Creation(No need to create object in every class only for all class)------
        fun getInstance():ApiService{
            val retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()

            return retrofit.create(ApiService::class.java)
        }
    }
    //http://27.109.19.234/metreat/backend/web/api/index?json_data={%22method%22:%22login%22,%22email%22:%22mphp.magneto1@gmail.com%22,%22password%22:%22123456%22,%22loginType%22:%22facebook%22,%22socialID%22:%22%22,%22deviceToken%22:%22%22,%20%22deviceType%22:%22ios%22,%20%22deviceEnvironment%22:%22production/sandbox%22}
    @GET("index")
    fun login(@Query("json_data") json_data: String): Call<Login>

    @GET()
    fun sum(@Query("a") a:Int):Call<Login>

    @GET("?")
    fun getMovieDetails(@Query("t") searchName:String,
                        @Query("apikey") key:String):Call<MovieBean>
}
